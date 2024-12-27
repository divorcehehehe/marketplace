package rom.biz.repo

import rom.biz.exceptions.ModelDbNotConfiguredException
import rom.common.Context
import rom.common.helpers.errorSystem
import rom.common.helpers.fail
import rom.common.models.WorkMode
import rom.common.repo.IRepoModel
import marketplace.cor.ICorChainDsl
import marketplace.cor.worker

fun ICorChainDsl<Context>.initRepo(title: String) = worker {
    this.title = title
    description = """
        Вычисление основного рабочего репозитория в зависимости от зпрошенного режима работы        
    """.trimIndent()
    handle {
        modelRepo = when {
            workMode == WorkMode.TEST -> corSettings.repoTest
            workMode == WorkMode.STUB -> corSettings.repoStub
            else -> corSettings.repoProd
        }
        if (workMode != WorkMode.STUB && modelRepo == IRepoModel.NONE) fail(
            errorSystem(
                violationCode = "dbNotConfigured",
                e = ModelDbNotConfiguredException(workMode)
            )
        )
    }
}
