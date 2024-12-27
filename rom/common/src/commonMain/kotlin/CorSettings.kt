package rom.common

import marketplace.logging.common.LoggerProvider
import rom.common.repo.IRepoModel

data class CorSettings(
    val loggerProvider: LoggerProvider = LoggerProvider(),
    val repoStub: IRepoModel = IRepoModel.NONE,
    val repoTest: IRepoModel = IRepoModel.NONE,
    val repoProd: IRepoModel = IRepoModel.NONE,
) {
    companion object {
        val NONE = CorSettings()
    }
}
