package rom.biz.repo

import rom.common.Context
import rom.common.models.State
import marketplace.cor.ICorChainDsl
import marketplace.cor.worker

fun ICorChainDsl<Context>.repoPrepareDelete(title: String) = worker {
    this.title = title
    description = """
        Готовим данные к удалению из БД
    """.trimIndent()
    on { state == State.RUNNING }
    handle {
        modelRepoPrepare = modelValidated.copy()
    }
}
