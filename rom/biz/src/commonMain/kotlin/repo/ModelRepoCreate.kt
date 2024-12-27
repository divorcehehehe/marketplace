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

fun ICorChainDsl<Context>.repoCreate(title: String) = worker {
    this.title = title
    description = "Добавление модели в БД"
    on { state == State.RUNNING.also { println("state: $state") } }
    handle {
        println("validated: ${modelRepoPrepare.requestUserId}")
        val request = DbModelRequest(modelRepoPrepare)
        val result = modelRepo.createModel(request)
        when(result) {
            is DbModelResponseOk -> modelRepoDone = result.data
            is DbModelResponseErr -> fail(result.errors)
            is DbModelResponseErrWithData -> fail(result.errors)
        }
    }
}
