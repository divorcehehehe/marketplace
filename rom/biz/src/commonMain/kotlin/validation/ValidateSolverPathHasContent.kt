package rom.biz.validation

import marketplace.cor.ICorChainDsl
import marketplace.cor.worker
import rom.common.helpers.errorValidation
import rom.common.Context
import rom.common.helpers.fail

fun ICorChainDsl<Context>.validateSolverPathHasContent(title: String) = worker {
    this.title = title
    this.description = """
        Проверяем валидность пути solverPath
    """.trimIndent()
    val invalidChars = Regex("[<>:\"|?*]")
    on { modelValidating.solverPath.isNotEmpty() && modelValidating.solverPath.contains(invalidChars) }
    handle {
        fail(
            errorValidation(
                field = "solverPath",
                violationCode = "badContent",
                description = "field must contain valid path"
            )
        )
    }
}
