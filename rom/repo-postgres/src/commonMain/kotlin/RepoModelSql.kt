package rom.backend.repo.postgresql

import com.benasher44.uuid.uuid4
import rom.common.models.Model
import rom.common.repo.*
import rom.repo.common.IRepoModelInitializable

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class RepoModelSql(
    properties: SqlProperties,
    randomUuid: () -> String = { uuid4().toString() },
) : IRepoModel, IRepoModelInitializable {
    override fun save(models: Collection<Model>): Collection<Model>
    override suspend fun createModel(rq: DbModelRequest): IDbModelResponse
    override suspend fun readModel(rq: DbModelIdRequest): IDbModelResponse
    override suspend fun updateModel(rq: DbModelRequest): IDbModelResponse
    override suspend fun deleteModel(rq: DbModelIdRequest): IDbModelResponse
    override suspend fun searchModel(rq: DbModelFilterRequest): IDbModelsResponse
    fun clear()
}
