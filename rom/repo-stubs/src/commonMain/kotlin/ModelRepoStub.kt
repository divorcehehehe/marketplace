package rom.backend.repository.inmemory

import rom.common.repo.*
import rom.stubs.ModelStub

class ModelRepoStub() : IRepoModel {
    override suspend fun createModel(rq: DbModelRequest): IDbModelResponse {
        return DbModelResponseOk(
            data = ModelStub.get(),
        )
    }

    override suspend fun readModel(rq: DbModelIdRequest): IDbModelResponse {
        return DbModelResponseOk(
            data = ModelStub.get(),
        )
    }

    override suspend fun updateModel(rq: DbModelRequest): IDbModelResponse {
        return DbModelResponseOk(
            data = ModelStub.get(),
        )
    }

    override suspend fun deleteModel(rq: DbModelIdRequest): IDbModelResponse {
        return DbModelResponseOk(
            data = ModelStub.get(),
        )
    }

    override suspend fun searchModel(rq: DbModelFilterRequest): IDbModelsResponse {
        return DbModelsResponseOk(
            data = ModelStub.prepareSearchList(filter = ""),
        )
    }
}
