package rom.biz.validation

import marketplace.cor.ICorChainDsl
import marketplace.cor.worker
import rom.common.helpers.errorValidation
import rom.common.Context
import rom.common.helpers.fail
import rom.common.models.UserId

fun ICorChainDsl<Context>.validateSearchRequestUserIdProperFormat(title: String) = worker {
    this.title = title

    // Может быть вынесен в ModelId для реализации различных форматов
    val regExp = Regex("^[0-9a-zA-Z#:-]+$")
    on {
        modelFilterValidating.requestUserId != UserId.NONE &&
        ! modelFilterValidating.requestUserId.asString().matches(regExp) }
    handle {
        val encodedId = modelFilterValidating.requestUserId.asString()
            .replace("<", "&lt;")
            .replace(">", "&gt;")
        fail(
            errorValidation(
                field = "requestUserId",
                violationCode = "badFormat",
                description = "value $encodedId must contain only letters and numbers"
            )
        )
    }
}
