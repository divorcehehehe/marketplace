package rom.biz.repo

import marketplace.cor.ICorChainDsl
import marketplace.cor.worker
import rom.common.helpers.errorValidation
import rom.common.Context
import rom.common.helpers.fail

fun ICorChainDsl<Context>.checkUSVTNotEmpty(title: String) = worker {
    this.title = title
    on {
        modelRepoRead.usVector.isEmpty() || modelRepoRead.vtVector.isEmpty()
    }
    handle {
        fail(
            errorValidation(
                field = "usVector, vtVector",
                violationCode = "empty",
                description = "you must train model before predicting"
            )
        )
    }
}
