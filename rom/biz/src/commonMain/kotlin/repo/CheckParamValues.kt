package rom.biz.repo

import rom.common.Context
import rom.common.helpers.fail
import rom.common.models.State
import marketplace.cor.ICorChainDsl
import marketplace.cor.worker
import rom.common.repo.errorBadParamValues

fun ICorChainDsl<Context>.checkParamValues(title: String) = worker {
    this.title = title
    description = """
        проверка paramValues
    """.trimIndent()
    on { state == State.RUNNING && modelValidated.paramValues.size != modelRepoRead.params.size }
    handle {
        fail(errorBadParamValues.errors)
    }
}
