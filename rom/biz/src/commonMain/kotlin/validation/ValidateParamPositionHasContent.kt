package rom.biz.validation

import marketplace.cor.ICorChainDsl
import marketplace.cor.worker
import rom.common.helpers.errorValidation
import rom.common.helpers.fail
import rom.common.models.Param
import rom.common.models.State

fun ICorChainDsl<Param>.validateParamPositionHasContent(title: String) = worker {
    this.title = title
    this.description = """
        Проверяем валидность поля position (position > 0)
    """.trimIndent()
    on { corSettings.parent.state == State.RUNNING && position < 1 }
    handle {
        corSettings.parent.fail(
            errorValidation(
                field = "params[${corSettings.paramIndex}].position",
                violationCode = "badContent",
                description = "field must be greater than zero"
            )
        )
    }
}
