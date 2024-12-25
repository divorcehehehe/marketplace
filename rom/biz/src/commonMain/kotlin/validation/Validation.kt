package rom.biz.validation

import rom.common.Context
import rom.common.models.State
import marketplace.cor.ICorChainDsl
import marketplace.cor.chain

fun ICorChainDsl<Context>.validation(block: ICorChainDsl<Context>.() -> Unit) = chain {
    block()
    title = "Валидация"

    on { state == State.RUNNING }
}
