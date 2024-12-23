package rom.biz.validation

import marketplace.cor.*
import rom.biz.ParamProcessor
import rom.common.helpers.errorValidation
import rom.common.Context
import rom.common.helpers.fail
import rom.common.models.*

fun ICorChainDsl<Context>.validateParamsHasContent(title: String) = worker {
    this.title = title
    this.description = """
        Проверяем валидность параметров
    """.trimIndent()
    on { modelValidating.params.isNotEmpty() }
    handle {
        val processor = ParamProcessor(ParamCorSettings(parent = this))
        modelValidating.params.forEachIndexed { index, _ ->
            processor.also { it.corSettings.paramIndex = index }.exec(modelValidating.params[index])
        }
    }
}
