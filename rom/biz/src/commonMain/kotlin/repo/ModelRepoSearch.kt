package rom.biz.repo

import rom.common.Context
import rom.common.helpers.fail
import rom.common.models.State
import rom.common.repo.DbModelFilterRequest
import rom.common.repo.DbModelsResponseErr
import rom.common.repo.DbModelsResponseOk
import marketplace.cor.ICorChainDsl
import marketplace.cor.worker

fun ICorChainDsl<Context>.repoSearch(title: String) = worker {
    this.title = title
    description = "Поиск моделей в БД по фильтру"
    on { state == State.RUNNING }
    handle {
        val request = DbModelFilterRequest(
            nameFilter = modelFilterValidated.searchString,
            requestUserId = modelFilterValidated.requestUserId,
        )
        when(val result = modelRepo.searchModel(request)) {
            is DbModelsResponseOk -> modelsRepoDone = result.data.toMutableList()
            is DbModelsResponseErr -> fail(result.errors)
        }
    }
}
