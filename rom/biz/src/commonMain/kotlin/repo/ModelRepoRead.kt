package rom.biz.repo

import rom.common.Context
import rom.common.helpers.fail
import rom.common.models.State
import rom.common.repo.DbModelIdRequest
import rom.common.repo.DbModelResponseErr
import rom.common.repo.DbModelResponseErrWithData
import rom.common.repo.DbModelResponseOk
import marketplace.cor.ICorChainDsl
import marketplace.cor.worker

fun ICorChainDsl<Context>.repoRead(title: String) = worker {
    this.title = title
    description = "Чтение модели из БД"
    on { state == State.RUNNING }
    handle {
        val request = DbModelIdRequest(modelValidated)
        when(val result = modelRepo.readModel(request)) {
            is DbModelResponseOk -> {
                modelRepoRead = result.data
                modelRepoRead.requestUserId = request.requestUserId
            }
            is DbModelResponseErr -> fail(result.errors)
            is DbModelResponseErrWithData -> {
                fail(result.errors)
                modelRepoRead = result.data
            }
        }
    }
}
