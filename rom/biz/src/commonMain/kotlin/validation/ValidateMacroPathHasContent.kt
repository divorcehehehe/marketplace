package rom.biz.validation

import marketplace.cor.ICorChainDsl
import marketplace.cor.worker
import rom.common.helpers.errorValidation
import rom.common.Context
import rom.common.helpers.fail

fun ICorChainDsl<Context>.validateMacroPathHasContent(title: String) = worker {
    this.title = title
    this.description = """
        Проверяем валидность пути macroPath
    """.trimIndent()
    val invalidChars = Regex("[<>:\"|?*]")
    on { modelValidating.macroPath.isNotEmpty() && modelValidating.macroPath.contains(invalidChars) }
    handle {
        fail(
            errorValidation(
                field = "macroPath",
                violationCode = "badContent",
                description = "field must contain valid path"
            )
        )
    }
}
