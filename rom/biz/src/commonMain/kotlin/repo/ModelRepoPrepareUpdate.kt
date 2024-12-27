package rom.biz.repo

import rom.common.Context
import rom.common.models.State
import marketplace.cor.ICorChainDsl
import marketplace.cor.worker

fun ICorChainDsl<Context>.repoPrepareUpdate(title: String) = worker {
    this.title = title
    description = "Готовим данные к сохранению в БД: совмещаем данные, прочитанные из БД, " +
            "и данные, полученные от пользователя"
    on { state == State.RUNNING }
    handle {
        modelRepoPrepare = modelRepoRead.copy().apply {
            requestUserId = modelValidated.requestUserId
            name = modelValidated.name
            macroPath = modelValidated.macroPath
            solverPath = modelValidated.solverPath
            params = modelValidated.params
            sampling = modelValidated.sampling
            visibility = modelValidated.visibility
        }
    }
}
