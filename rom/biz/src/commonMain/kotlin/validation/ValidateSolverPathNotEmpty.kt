package rom.biz.validation

import marketplace.cor.ICorChainDsl
import marketplace.cor.worker
import rom.common.helpers.errorValidation
import rom.common.Context
import rom.common.helpers.fail

fun ICorChainDsl<Context>.validateSolverPathNotEmpty(title: String) = worker {
    this.title = title
    this.description = """
        Проверяем наличие пути solverPath
    """.trimIndent()
    on { modelValidating.solverPath.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "solverPath",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
