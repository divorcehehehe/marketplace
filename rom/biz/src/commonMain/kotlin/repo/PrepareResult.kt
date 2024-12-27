package rom.biz.repo

import rom.common.Context
import rom.common.models.State
import rom.common.models.WorkMode
import marketplace.cor.ICorChainDsl
import marketplace.cor.worker

fun ICorChainDsl<Context>.prepareResult(title: String) = worker {
    this.title = title
    description = "Подготовка данных для ответа клиенту на запрос"
    on { workMode != WorkMode.STUB }
    handle {
        modelResponse = modelRepoDone
        modelsResponse = modelsRepoDone
        state = when (val st = state) {
            State.RUNNING -> State.FINISHING
            else -> st
        }
    }
}
