package rom.biz.stubs

import marketplace.cor.ICorChainDsl
import marketplace.cor.worker
import rom.common.Context
import rom.common.helpers.fail
import rom.common.models.ROMError
import rom.common.models.State
import rom.common.stubs.Stubs

fun ICorChainDsl<Context>.stubValidationBadSearchString(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации поискового запроса
    """.trimIndent()

    on { stubCase == Stubs.BAD_SEARCH_STRING && state == State.RUNNING }
    handle {
        fail(
            ROMError(
                group = "validation",
                code = "validation-searchString",
                field = "searchString",
                message = "Wrong searchString field"
            )
        )
    }
}
