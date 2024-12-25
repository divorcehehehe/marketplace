package rom.biz.validation

import rom.common.helpers.errorValidation
import rom.common.Context
import rom.common.helpers.fail
import marketplace.cor.ICorChainDsl
import marketplace.cor.worker

fun ICorChainDsl<Context>.validateLockNotEmpty(title: String) = worker {
    this.title = title
    on { modelValidating.lock.asString().isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "lock",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
