package rom.biz.validation

import marketplace.cor.ICorChainDsl
import marketplace.cor.worker
import rom.common.helpers.errorValidation
import rom.common.helpers.fail
import rom.common.models.Param
import rom.common.models.State

fun ICorChainDsl<Param>.validateParamLineHasContent(title: String) = worker {
    this.title = title
    this.description = """
        Проверяем валидность поля line (line > 0)
    """.trimIndent()
    on { corSettings.parent.state == State.RUNNING && line < 1 }
    handle {
        println("validation fails")
        corSettings.parent.fail(
            errorValidation(
                field = "params[${corSettings.paramIndex}].line",
                violationCode = "badContent",
                description = "field must be greater than zero"
            )
        )
    }
    build()
}
