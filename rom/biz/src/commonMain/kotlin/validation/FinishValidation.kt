package rom.biz.validation

import rom.common.Context
import rom.common.models.State
import marketplace.cor.ICorChainDsl
import marketplace.cor.worker

fun ICorChainDsl<Context>.finishModelValidation(title: String) = worker {
    this.title = title
    on { state == State.RUNNING }
    handle {
        modelValidated = modelValidating.deepCopy()
    }
}

fun ICorChainDsl<Context>.finishModelFilterValidation(title: String) = worker {
    this.title = title
    on { state == State.RUNNING }
    handle {
        modelFilterValidated = modelFilterValidating.deepCopy()
    }
}
