package rom.biz.stubs

import marketplace.cor.ICorChainDsl
import marketplace.cor.worker
import rom.common.Context
import rom.common.helpers.fail
import rom.common.models.ROMError
import rom.common.models.State
import rom.common.stubs.Stubs

fun ICorChainDsl<Context>.stubValidationBadParamBounds(title: String) = worker {
    this.title = title
    this.description = """
        Кейс ошибки валидации для границ диапозона варьирования параметра
    """.trimIndent()

    on { stubCase == Stubs.BAD_PARAM_BOUNDS && state == State.RUNNING }
    handle {
        fail(
            ROMError(
                group = "validation",
                code = "validation-param.bounds",
                field = "param.bounds",
                message = "Wrong bounds field"
            )
        )
    }
}
