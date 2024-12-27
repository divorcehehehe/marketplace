package rom.backend.repo.tests

import rom.common.models.Model
import rom.common.repo.*

class ModelRepositoryMock(
    private val invokeCreateModel: (DbModelRequest) -> IDbModelResponse = { DEFAULT_MODEL_SUCCESS_EMPTY_MOCK },
    private val invokeReadModel: (DbModelIdRequest) -> IDbModelResponse = { DEFAULT_MODEL_SUCCESS_EMPTY_MOCK },
    private val invokeUpdateModel: (DbModelRequest) -> IDbModelResponse = { DEFAULT_MODEL_SUCCESS_EMPTY_MOCK },
    private val invokeDeleteModel: (DbModelIdRequest) -> IDbModelResponse = { DEFAULT_MODEL_SUCCESS_EMPTY_MOCK },
    private val invokeSearchModel: (DbModelFilterRequest) -> IDbModelsResponse = { DEFAULT_MODELS_SUCCESS_EMPTY_MOCK },
): IRepoModel {
    override suspend fun createModel(rq: DbModelRequest): IDbModelResponse {
        return invokeCreateModel(rq)
    }

    override suspend fun readModel(rq: DbModelIdRequest): IDbModelResponse {
        return invokeReadModel(rq)
    }

    override suspend fun updateModel(rq: DbModelRequest): IDbModelResponse {
        return invokeUpdateModel(rq)
    }

    override suspend fun deleteModel(rq: DbModelIdRequest): IDbModelResponse {
        return invokeDeleteModel(rq)
    }

    override suspend fun searchModel(rq: DbModelFilterRequest): IDbModelsResponse {
        return invokeSearchModel(rq)
    }

    companion object {
        val DEFAULT_MODEL_SUCCESS_EMPTY_MOCK = DbModelResponseOk(Model())
        val DEFAULT_MODELS_SUCCESS_EMPTY_MOCK = DbModelsResponseOk(emptyList())
    }
}
