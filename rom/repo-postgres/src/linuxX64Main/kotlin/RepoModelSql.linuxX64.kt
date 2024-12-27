package rom.backend.repo.postgresql

import rom.common.models.Model
import rom.common.repo.*
import rom.repo.common.IRepoModelInitializable

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class RepoModelSql actual constructor(
    properties: SqlProperties,
    randomUuid: () -> String
) : IRepoModel, IRepoModelInitializable {
    actual override fun save(models: Collection<Model>): Collection<Model> {
        TODO("Not yet implemented")
    }

    actual override suspend fun createModel(rq: DbModelRequest): IDbModelResponse {
        TODO("Not yet implemented")
    }

    actual override suspend fun readModel(rq: DbModelIdRequest): IDbModelResponse {
        TODO("Not yet implemented")
    }

    actual override suspend fun updateModel(rq: DbModelRequest): IDbModelResponse {
        TODO("Not yet implemented")
    }

    actual override suspend fun deleteModel(rq: DbModelIdRequest): IDbModelResponse {
        TODO("Not yet implemented")
    }

    actual override suspend fun searchModel(rq: DbModelFilterRequest): IDbModelsResponse {
        TODO("Not yet implemented")
    }

    actual fun clear() {
        TODO("Not yet implemented")
    }
}
