package rom.biz.validation

import marketplace.cor.ICorChainDsl
import marketplace.cor.worker
import rom.common.helpers.errorValidation
import rom.common.Context
import rom.common.helpers.fail
import rom.common.models.Visibility

fun ICorChainDsl<Context>.validateVisibilityNotEmpty(title: String) = worker {
    this.title = title
    on { modelValidating.visibility == Visibility.NONE }
    handle {
        fail(
            errorValidation(
                field = "visibility",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
