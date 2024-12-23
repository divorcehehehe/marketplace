package rom.biz.validation

import marketplace.cor.ICorChainDsl
import marketplace.cor.worker
import rom.common.helpers.errorValidation
import rom.common.Context
import rom.common.helpers.fail
import rom.common.models.ModelId

fun ICorChainDsl<Context>.validateIdProperFormat(title: String) = worker {
    this.title = title

    // Может быть вынесен в ModelId для реализации различных форматов
    val regExp = Regex("^[0-9a-zA-Z#:-]+$")
    on { modelValidating.id != ModelId.NONE && ! modelValidating.id.asString().matches(regExp) }
    handle {
        val encodedId = modelValidating.id.asString()
            .replace("<", "&lt;")
            .replace(">", "&gt;")
        fail(
            errorValidation(
                field = "id",
                violationCode = "badFormat",
                description = "value $encodedId must contain only letters and numbers"
            )
        )
    }
}
