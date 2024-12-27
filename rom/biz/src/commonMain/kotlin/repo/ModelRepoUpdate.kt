package rom.biz.repo

import rom.common.Context
import rom.common.helpers.fail
import rom.common.models.State
import rom.common.repo.DbModelRequest
import rom.common.repo.DbModelResponseErr
import rom.common.repo.DbModelResponseErrWithData
import rom.common.repo.DbModelResponseOk
import marketplace.cor.ICorChainDsl
import marketplace.cor.worker

fun ICorChainDsl<Context>.repoUpdate(title: String) = worker {
    this.title = title
    on { state == State.RUNNING }
    handle {
        val request = DbModelRequest(modelRepoPrepare)
        when(val result = modelRepo.updateModel(request)) {
            is DbModelResponseOk -> modelRepoDone = result.data
            is DbModelResponseErr -> fail(result.errors)
            is DbModelResponseErrWithData -> {
                fail(result.errors)
                modelRepoDone = result.data
            }
        }
    }
}
