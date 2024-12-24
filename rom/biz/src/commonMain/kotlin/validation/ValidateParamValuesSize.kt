package rom.biz.validation

import marketplace.cor.ICorChainDsl
import marketplace.cor.worker
import rom.common.helpers.errorValidation
import rom.common.Context
import rom.common.helpers.fail

fun ICorChainDsl<Context>.validateParamValuesSize(title: String) = worker {
    this.title = title
    on { modelValidating.paramValues.size != modelValidating.params.size }
    handle {
        fail(
            errorValidation(
                field = "paramValues",
                violationCode = "wrongSize",
                description = "field must be as long as params field"
            )
        )
    }
}
