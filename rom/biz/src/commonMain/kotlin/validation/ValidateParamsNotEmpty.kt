package rom.biz.validation

import marketplace.cor.ICorChainDsl
import marketplace.cor.worker
import rom.common.helpers.errorValidation
import rom.common.Context
import rom.common.helpers.fail

fun ICorChainDsl<Context>.validateParamsNotEmpty(title: String) = worker {
    this.title = title
    this.description = """
        Проверяем наличие параметров
    """.trimIndent()
    on { modelValidating.params.isEmpty() }
    handle {
        fail(
            errorValidation(
                field = "params",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
