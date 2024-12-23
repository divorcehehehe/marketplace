package rom.biz.validation

import marketplace.cor.ICorChainDsl
import marketplace.cor.worker
import rom.common.helpers.errorValidation
import rom.common.Context
import rom.common.helpers.fail

fun ICorChainDsl<Context>.validateNameHasContent(title: String) = worker {
    this.title = title
    this.description = """
        Проверяем, что у нас есть какие-то слова в имени модели.
        Отказываем в публикации моделей, в именах которых только бессмысленные символы типа %^&^$^%#^))&^*&%^^&
    """.trimIndent()
    val regExp = Regex("\\p{L}")
    on { modelValidating.name.isNotEmpty() && !modelValidating.name.contains(regExp) }
    handle {
        fail(
            errorValidation(
                field = "name",
                violationCode = "badContent",
                description = "field must contain letters"
            )
        )
    }
}
