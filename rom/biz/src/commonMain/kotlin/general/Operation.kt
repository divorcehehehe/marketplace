package rom.biz.general

import rom.common.Context
import rom.common.models.Command
import rom.common.models.State
import marketplace.cor.ICorChainDsl
import marketplace.cor.chain

fun ICorChainDsl<Context>.operation(
    title: String,
    command: Command,
    block: ICorChainDsl<Context>.() -> Unit
) = chain {
    block()
    this.title = title
    on { this.command == command && state == State.RUNNING }
}
