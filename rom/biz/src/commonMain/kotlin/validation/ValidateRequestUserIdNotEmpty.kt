package rom.biz.validation

import marketplace.cor.ICorChainDsl
import marketplace.cor.worker
import rom.common.helpers.errorValidation
import rom.common.Context
import rom.common.helpers.fail
import rom.common.models.UserId

fun ICorChainDsl<Context>.validateRequestUserIdNotEmpty(title: String) = worker {
    this.title = title
    on { modelValidating.requestUserId == UserId.NONE }
    handle {
        fail(
            errorValidation(
                field = "requestUserId",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
