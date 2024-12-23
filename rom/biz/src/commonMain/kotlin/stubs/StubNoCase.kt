package rom.biz.stubs

import marketplace.cor.ICorChainDsl
import marketplace.cor.worker
import rom.common.Context
import rom.common.helpers.fail
import rom.common.models.ROMError
import rom.common.models.State

fun ICorChainDsl<Context>.stubNoCase(title: String) = worker {
    this.title = title
    this.description = """
        Валидируем ситуацию, когда запрошен кейс, который не поддерживается в стабах
    """.trimIndent()
    on { state == State.RUNNING }
    handle {
        fail(
            ROMError(
                code = "validation",
                group = "validation",
                field = "stub",
                message = "Wrong stub case is requested: ${stubCase.name}"
            )
        )
    }
}
