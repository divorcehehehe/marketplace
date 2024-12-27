package rom.biz.validation

import marketplace.cor.ICorChainDsl
import marketplace.cor.worker
import rom.common.helpers.errorValidation
import rom.common.Context
import rom.common.helpers.fail

fun ICorChainDsl<Context>.validateParamValuesNotEmpty(title: String) = worker {
    this.title = title
    on { modelValidating.paramValues.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "paramValues",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
