package rom.biz.stubs

import marketplace.cor.ICorChainDsl
import marketplace.cor.worker
import rom.common.Context
import rom.common.helpers.fail
import rom.common.models.ROMError
import rom.common.models.State
import rom.common.stubs.Stubs

fun ICorChainDsl<Context>.stubValidationBadMacroPath(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации для пути к макросу
    """.trimIndent()

    on { stubCase == Stubs.BAD_MACRO_PATH && state == State.RUNNING }
    handle {
        fail(
            ROMError(
                group = "validation",
                code = "validation-macroPath",
                field = "macroPath",
                message = "Wrong macro path field"
            )
        )
    }
}
