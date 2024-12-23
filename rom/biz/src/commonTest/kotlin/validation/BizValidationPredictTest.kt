package rom.biz.validation

import rom.common.models.Command
import kotlin.test.Test

class BizValidationPredictTest: BaseBizValidationTest() {
    override val command = Command.READ

    @Test fun correctId() = validationIdCorrect(command, processor)
    @Test fun trimId() = validationIdTrim(command, processor)
    @Test fun emptyId() = validationIdEmpty(command, processor)
    @Test fun badFormatId() = validationIdFormat(command, processor)

}
