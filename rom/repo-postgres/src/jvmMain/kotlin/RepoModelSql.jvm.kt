package rom.backend.repo.postgresql

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import rom.common.helpers.asError
import rom.common.models.*
import rom.common.repo.*
import rom.repo.common.IRepoModelInitializable

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class RepoModelSql actual constructor(
    properties: SqlProperties,
    private val randomUuid: () -> String
) : IRepoModel, IRepoModelInitializable {
    private val modelTable = ModelTable("${properties.schema}.${properties.table}")

    private val driver = when {
        properties.url.startsWith("jdbc:postgresql://") -> "org.postgresql.Driver"
        else -> throw IllegalArgumentException("Unknown driver for url ${properties.url}")
    }

    private val conn = Database.connect(
        properties.url, driver, properties.user, properties.password
    )

    actual fun clear(): Unit = transaction(conn) {
        modelTable.deleteAll()
    }

    private fun saveObj(model: Model): Model = transaction(conn) {
        val res = modelTable
            .insert {
                to(it, model, randomUuid)
            }
            .resultedValues
            ?.map { modelTable.from(it) }
        res?.first() ?: throw RuntimeException("BD error: insert statement returned empty result")
    }

    private suspend inline fun <T> transactionWrapper(crossinline block: () -> T, crossinline handle: (Exception) -> T): T =
        withContext(Dispatchers.IO) {
            try {
                transaction(conn) {
                    block()
                }
            } catch (e: Exception) {
                handle(e)
            }
        }

    private suspend inline fun transactionWrapper(crossinline block: () -> IDbModelResponse): IDbModelResponse =
        transactionWrapper(block) { DbModelResponseErr(it.asError()) }

    actual override fun save(models: Collection<Model>): Collection<Model> = models.map { saveObj(it) }
    actual override suspend fun createModel(rq: DbModelRequest): IDbModelResponse = transactionWrapper {
        DbModelResponseOk(saveObj(rq.model))
    }

    private fun read(id: ModelId, rui: UserId): IDbModelResponse {
        if (id == ModelId.NONE) return errorEmptyId
        if (rui == UserId.NONE) return errorRequestUserId(id)

        val res = modelTable.selectAll().where {
            modelTable.id eq id.asString()
        }.singleOrNull() ?: return errorNotFound(id)

        if (
            res[modelTable.ownerId] != rui.asString() &&
            res[modelTable.visibility] != Visibility.VISIBLE_PUBLIC
        ) {
            return errorVisibility(id, "read")
        }

        return DbModelResponseOk(modelTable.from(res).apply { requestUserId = rui })
    }

    actual override suspend fun readModel(rq: DbModelIdRequest): IDbModelResponse = transactionWrapper {
        read(rq.id, rq.requestUserId)
    }

    private suspend fun update(
        id: ModelId,
        lock: ModelLock,
        rui: UserId,
        block: (Model) -> IDbModelResponse,
    ): IDbModelResponse =
        transactionWrapper {
            if (id == ModelId.NONE) return@transactionWrapper errorEmptyId
            if (lock == ModelLock.NONE) return@transactionWrapper errorEmptyLock(id)
            if (rui == UserId.NONE) return@transactionWrapper errorRequestUserId(id)

            val current = modelTable.selectAll().where { modelTable.id eq id.asString() }
                .singleOrNull()
                ?.let { modelTable.from(it) }

            when {
                current == null -> errorNotFound(id)
                current.ownerId != rui && current.visibility != Visibility.VISIBLE_PUBLIC ->
                    errorVisibility(id, "update")
                current.lock != lock -> errorRepoConcurrency(current, lock)
                else -> block(current)
            }
        }

    actual override suspend fun updateModel(rq: DbModelRequest): IDbModelResponse = update(
        rq.model.id,
        rq.model.lock,
        rq.model.requestUserId,
    ) {
        modelTable.update({ modelTable.id eq rq.model.id.asString() }) {
            to(it, rq.model.copy(lock = ModelLock(randomUuid()),
                ownerId = UserId.NONE), randomUuid)
        }
        read(rq.model.id, rq.model.requestUserId)
    }

    actual override suspend fun deleteModel(rq: DbModelIdRequest): IDbModelResponse = update(
        rq.id,
        rq.lock,
        rq.requestUserId
    ) {
        modelTable.deleteWhere { id eq rq.id.asString() }
        DbModelResponseOk(it.apply { requestUserId = rq.requestUserId })
    }

    actual override suspend fun searchModel(rq: DbModelFilterRequest): IDbModelsResponse =
        transactionWrapper({
            if (rq.requestUserId == UserId.NONE) return@transactionWrapper errorRequestUserId()

            val res = modelTable.selectAll().where {
                buildList {
                    if (rq.nameFilter.isNotBlank()) {
                        add(
                            (modelTable.name like "%${rq.nameFilter}%")
                        )
                    }
                    add(
                        (modelTable.ownerId eq rq.requestUserId.asString()) or
                            (modelTable.visibility eq Visibility.VISIBLE_PUBLIC)
                    )
                }.reduce { a, b -> a and b }
            }
            DbModelsResponseOk(data = res.map { modelTable.from(it) })
        }, {
            DbModelsResponseErr(it.asError())
        })
}
