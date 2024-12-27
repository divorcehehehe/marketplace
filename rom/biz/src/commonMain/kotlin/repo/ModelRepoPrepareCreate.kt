package rom.biz.repo

import rom.common.Context
import rom.common.models.State
import marketplace.cor.ICorChainDsl
import marketplace.cor.worker

fun ICorChainDsl<Context>.repoPrepareCreate(title: String) = worker {
    this.title = title
    description = "Подготовка объекта к сохранению в базе данных"
    on { state == State.RUNNING }
    handle {
        modelRepoPrepare = modelValidated.copy()
        modelRepoPrepare.ownerId = modelRepoPrepare.requestUserId
    }
}
