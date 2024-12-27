package rom.biz.validation

import rom.biz.ModelProcessor
import rom.common.CorSettings
import rom.common.models.Command
import rom.repo.common.ModelRepoInitialized
import rom.repo.inmemory.ModelRepoInMemory
import rom.stubs.ModelStub

abstract class BaseBizValidationTest {
    protected abstract val command: Command
    private val repo = ModelRepoInitialized(
        repo = ModelRepoInMemory(),
        initObjects = listOf(
            ModelStub.get(),
        ),
    )
    private val settings by lazy { CorSettings(repoTest = repo) }
    protected val processor by lazy { ModelProcessor(settings) }
}
