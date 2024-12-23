package rom.biz.validation

import marketplace.cor.ICorChainDsl
import marketplace.cor.worker
import rom.common.helpers.errorValidation
import rom.common.helpers.fail
import rom.common.models.Param
import rom.common.models.State

fun ICorChainDsl<Param>.validateParamBoundsSize(title: String) = worker {
    this.title = title
    this.description = """
        Проверяем валидность поля bounds:
            bounds.size = 2
    """.trimIndent()
    on { corSettings.parent.state == State.RUNNING && bounds.size != 2 }
    handle {
        corSettings.parent.fail(
            errorValidation(
                field = "params[${corSettings.paramIndex}].bounds",
                violationCode = "badContent",
                description = "field must be the mutable list of size 2"
            )
        )
    }
}
