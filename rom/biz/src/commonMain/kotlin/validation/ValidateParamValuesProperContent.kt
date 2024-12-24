package rom.biz.validation

import marketplace.cor.ICorChainDsl
import marketplace.cor.worker
import rom.common.helpers.errorValidation
import rom.common.Context
import rom.common.helpers.fail

fun ICorChainDsl<Context>.validateParamValuesProperContent(title: String) = worker {
    this.title = title
    on { modelValidating.paramValues.size == modelValidating.params.size }
    handle {
        modelValidating.paramValues.forEachIndexed { index, value ->
            this@validateParamValuesProperContent.worker {
                on { modelValidating.params[index].bounds[0] > value }
                handle {
                    fail(
                        errorValidation(
                            field = "paramValues[$index]",
                            violationCode = "badContent",
                            description = "field must be greater than params[$index].bounds[0]"
                        )
                    )
                }
            }

            this@validateParamValuesProperContent.worker {
                on { modelValidating.params[index].bounds[1] < value }
                handle {
                    fail(
                        errorValidation(
                            field = "paramValues[$index]",
                            violationCode = "badContent",
                            description = "field must be less than or equal to params[$index].bounds[1]"
                        )
                    )
                }
            }
        }
    }
}
