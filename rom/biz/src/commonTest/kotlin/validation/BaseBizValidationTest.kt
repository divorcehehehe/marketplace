package rom.biz.validation

import rom.biz.ModelProcessor
import rom.common.CorSettings
import rom.common.models.Command

abstract class BaseBizValidationTest {
    protected abstract val command: Command
    private val settings by lazy { CorSettings() }
    protected val processor by lazy { ModelProcessor(settings) }
}
