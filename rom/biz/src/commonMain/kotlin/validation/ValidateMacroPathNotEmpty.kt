package rom.biz.validation

import marketplace.cor.ICorChainDsl
import marketplace.cor.worker
import rom.common.helpers.errorValidation
import rom.common.Context
import rom.common.helpers.fail

fun ICorChainDsl<Context>.validateMacroPathNotEmpty(title: String) = worker {
    this.title = title
    this.description = """
        Проверяем наличие пути macroPath
    """.trimIndent()
    on { modelValidating.macroPath.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "macroPath",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
