package rom.repo.inmemory

import com.benasher44.uuid.uuid4
import io.github.reactivecircus.cache4k.Cache
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import rom.common.models.*
import rom.common.repo.*
import rom.common.repo.exceptions.RepoEmptyLockException
import rom.repo.common.IRepoModelInitializable
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes

class ModelRepoInMemory(
    ttl: Duration = 2.minutes,
    val randomUuid: () -> String = { uuid4().toString() },
) : ModelRepoBase(), IRepoModel, IRepoModelInitializable {

    private val mutex: Mutex = Mutex()
    private val cache = Cache.Builder<String, ModelEntity>()
        .expireAfterWrite(ttl)
        .build()

    override fun save(models: Collection<Model>) = models.map { model ->
        val entity = ModelEntity(model)
        require(entity.id != null)
        cache.put(entity.id, entity)
        model
    }

    override suspend fun createModel(rq: DbModelRequest): IDbModelResponse = tryModelMethod {
        val key = randomUuid()
        val model = rq.model.copy(id = ModelId(key), lock = ModelLock(randomUuid()))
        model.params.forEach {
            it.modelId = ModelId(key)
            it.paramId = ParamId(randomUuid())
        }
        val entity = ModelEntity(model)
        mutex.withLock {
            cache.put(key, entity)
        }
        DbModelResponseOk(model)
    }

    override suspend fun readModel(rq: DbModelIdRequest): IDbModelResponse = tryModelMethod {
        val key = rq.id.takeIf { it != ModelId.NONE }?.asString() ?: return@tryModelMethod errorEmptyId
        val rui = rq.requestUserId.takeIf { it != UserId.NONE } ?: return@tryModelMethod errorRequestUserId(rq.id)

        mutex.withLock {
            cache.get(key)?.let { modelEntity ->
                DbModelResponseOk(modelEntity.toInternal()).takeIf {
                    rui == it.data.ownerId || it.data.visibility == Visibility.VISIBLE_PUBLIC
                } ?: errorVisibility(rq.id, "read")
            } ?: errorNotFound(rq.id)
        }
    }

    override suspend fun updateModel(rq: DbModelRequest): IDbModelResponse = tryModelMethod {
        val rqModel = rq.model
        val id = rqModel.id.takeIf { it != ModelId.NONE } ?: return@tryModelMethod errorEmptyId
        val oldLock = rqModel.lock.takeIf { it != ModelLock.NONE } ?: return@tryModelMethod errorEmptyLock(id)
        val rui = rqModel.requestUserId.takeIf { it != UserId.NONE } ?: return@tryModelMethod errorRequestUserId(id)

        val key = id.asString()

        mutex.withLock {
            val oldModel = cache.get(key)?.toInternal()
            when {
                oldModel == null -> errorNotFound(id)
                oldModel.ownerId != rui &&
                    oldModel.visibility != Visibility.VISIBLE_PUBLIC -> errorVisibility(id, "update")
                oldModel.lock == ModelLock.NONE -> errorDb(RepoEmptyLockException(id))
                oldModel.lock != oldLock -> errorRepoConcurrency(oldModel, oldLock)
                else -> {
                    rq.model.params.forEach {
                        it.modelId = id
                        it.paramId = ParamId(randomUuid())
                    }
                    val newModel = rq.model.copy(
                        lock = ModelLock(randomUuid()),
                        ownerId = oldModel.ownerId
                    )
                    val entity = ModelEntity(newModel)
                    cache.put(key, entity)
                    DbModelResponseOk(newModel)
                }
            }
        }
    }

    override suspend fun deleteModel(rq: DbModelIdRequest): IDbModelResponse = tryModelMethod {
        val id = rq.id.takeIf { it != ModelId.NONE } ?: return@tryModelMethod errorEmptyId
        val key = id.asString()
        val oldLock = rq.lock.takeIf { it != ModelLock.NONE } ?: return@tryModelMethod errorEmptyLock(id)
        val rui = rq.requestUserId.takeIf { it != UserId.NONE } ?: return@tryModelMethod errorRequestUserId(id)

        mutex.withLock {
            val oldModel = cache.get(key)?.toInternal()
            when {
                oldModel == null -> errorNotFound(id)
                oldModel.ownerId != rui &&
                    oldModel.visibility != Visibility.VISIBLE_PUBLIC -> errorVisibility(id, "delete")
                oldModel.lock == ModelLock.NONE -> errorDb(RepoEmptyLockException(id))
                oldModel.lock != oldLock -> errorRepoConcurrency(oldModel, oldLock)
                else -> {
                    cache.invalidate(key)
                    DbModelResponseOk(oldModel)
                }
            }
        }
    }

    override suspend fun searchModel(rq: DbModelFilterRequest): IDbModelsResponse = tryModelsMethod {
        val rui = rq.requestUserId.takeIf { it != UserId.NONE } ?: return@tryModelsMethod errorRequestUserId()

        val result: List<Model> = cache.asMap().asSequence()
            .filter { entry ->
                rq.nameFilter.takeIf { it.isNotBlank() }?.let {
                    entry.value.name?.contains(it) ?: false
                } ?: true
            }
            .filter { entry ->
                rui.let {
                    entry.value.ownerId?.equals(it.asString()) ?: false ||
                        entry.value.visibility?.equals(Visibility.VISIBLE_PUBLIC.name) ?: false
                }
            }
            .map { it.value.toInternal() }
            .toList()
        DbModelsResponseOk(result)
    }
}
