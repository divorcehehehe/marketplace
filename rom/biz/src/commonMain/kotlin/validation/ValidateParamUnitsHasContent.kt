package rom.biz.validation

import marketplace.cor.ICorChainDsl
import marketplace.cor.worker
import rom.common.helpers.errorValidation
import rom.common.helpers.fail
import rom.common.models.Param
import rom.common.models.State

fun ICorChainDsl<Param>.validateParamUnitsHasContent(title: String) = worker {
    this.title = title
    this.description = """
        Проверяем, что у нас есть какие-то буквы в поле units.
        Отказываем в публикации моделей, в поле units которых только бессмысленные символы типа %^&^$^%#^))&^*&%^^&
    """.trimIndent()
    val regExp = Regex("\\p{L}")
    on { corSettings.parent.state == State.RUNNING && units.isNotEmpty() && !units.contains(regExp) }
    handle {
        corSettings.parent.fail(
            errorValidation(
                field = "params[${corSettings.paramIndex}].units",
                violationCode = "badContent",
                description = "field must contain letters"
            )
        )
    }
}
