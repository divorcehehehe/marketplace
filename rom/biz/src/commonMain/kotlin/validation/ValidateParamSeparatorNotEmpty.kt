package rom.biz.validation

import marketplace.cor.ICorChainDsl
import marketplace.cor.worker
import rom.common.helpers.errorValidation
import rom.common.helpers.fail
import rom.common.models.Param
import rom.common.models.State

fun ICorChainDsl<Param>.validateParamSeparatorNotEmpty(title: String) = worker {
    this.title = title
    this.description = """
        Проверяем наличия поля separator
    """.trimIndent()
    on { corSettings.parent.state == State.RUNNING && separator.isEmpty() }
    handle {
        corSettings.parent.fail(
            errorValidation(
                field = "params[${corSettings.paramIndex}].separator",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
