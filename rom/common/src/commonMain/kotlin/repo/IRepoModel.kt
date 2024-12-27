package rom.common.repo

interface IRepoModel {
    suspend fun createModel(rq: DbModelRequest): IDbModelResponse
    suspend fun readModel(rq: DbModelIdRequest): IDbModelResponse
    suspend fun updateModel(rq: DbModelRequest): IDbModelResponse
    suspend fun deleteModel(rq: DbModelIdRequest): IDbModelResponse
    suspend fun searchModel(rq: DbModelFilterRequest): IDbModelsResponse

    companion object {
        val NONE = object : IRepoModel {
            override suspend fun createModel(rq: DbModelRequest): IDbModelResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun readModel(rq: DbModelIdRequest): IDbModelResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun updateModel(rq: DbModelRequest): IDbModelResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun deleteModel(rq: DbModelIdRequest): IDbModelResponse {
                throw NotImplementedError("Must not be used")
            }

            override suspend fun searchModel(rq: DbModelFilterRequest): IDbModelsResponse {
                throw NotImplementedError("Must not be used")
            }
        }
    }
}
