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

fun ICorChainDsl<Context>.repoDelete(title: String) = worker {
    this.title = title
    description = "Удаление модели из БД по ID"
    on { state == State.RUNNING }
    handle {
        val request = DbModelIdRequest(modelRepoPrepare)
        when(val result = modelRepo.deleteModel(request)) {
            is DbModelResponseOk -> modelRepoDone = result.data
            is DbModelResponseErr -> {
                fail(result.errors)
                modelRepoDone = modelRepoRead
            }
            is DbModelResponseErrWithData -> {
                fail(result.errors)
                modelRepoDone = result.data
            }
        }
    }
}
