package rom.biz.stubs

import rom.common.Context
import rom.common.models.State
import rom.common.models.WorkMode
import marketplace.cor.ICorChainDsl
import marketplace.cor.chain

fun ICorChainDsl<Context>.stubs(title: String, block: ICorChainDsl<Context>.() -> Unit) = chain {
    block()
    this.title = title
    on { workMode == WorkMode.STUB && state == State.RUNNING }
}
