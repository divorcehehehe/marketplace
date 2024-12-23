package rom.biz.stubs

import marketplace.cor.ICorChainDsl
import marketplace.cor.worker
import rom.common.Context
import rom.common.helpers.fail
import rom.common.models.ROMError
import rom.common.models.State
import rom.common.stubs.Stubs

fun ICorChainDsl<Context>.stubValidationBadName(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации для имени модели
    """.trimIndent()

    on { stubCase == Stubs.BAD_NAME && state == State.RUNNING }
    handle {
        fail(
            ROMError(
                group = "validation",
                code = "validation-name",
                field = "name",
                message = "Wrong name field"
            )
        )
    }
}
