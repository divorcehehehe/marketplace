package rom.biz.validation

import marketplace.cor.ICorChainDsl
import marketplace.cor.worker
import rom.common.helpers.errorValidation
import rom.common.helpers.fail
import rom.common.models.Param
import rom.common.models.State

fun ICorChainDsl<Param>.validateParamUnitsNotEmpty(title: String) = worker {
    this.title = title
    this.description = """
        Проверяем наличие поля units
    """.trimIndent()
    on { corSettings.parent.state == State.RUNNING && units.isEmpty() }
    handle {
        corSettings.parent.fail(
            errorValidation(
                field = "params[${corSettings.paramIndex}].units",
                violationCode = "empty",
                description = "field must not be empty"
            )
        )
    }
}
