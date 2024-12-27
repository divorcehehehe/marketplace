package rom.biz.general

import marketplace.cor.ICorChainDsl
import marketplace.cor.worker
import rom.common.Context
import rom.common.models.State

fun ICorChainDsl<Context>.initStatus(title: String) = worker {
    this.title = title
    this.description = """
        Этот обработчик устанавливает стартовый статус обработки. Запускается только в случае не заданного статуса.
    """.trimIndent()
    on { state == State.NONE }
    handle { state = State.RUNNING }
}
