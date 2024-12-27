package rom.biz.validation

import rom.common.models.Command
import kotlin.test.Test

class BizValidationPredictTest: BaseBizValidationTest() {
    override val command = Command.PREDICT

    @Test fun correctId() = validationIdCorrect(command, processor)
    @Test fun trimId() = validationIdTrim(command, processor)
    @Test fun emptyId() = validationIdEmpty(command, processor)
    @Test fun badFormatId() = validationIdFormat(command, processor)

    @Test fun correctRequestUserId() = validationRequestUserIdCorrect(command, processor)
    @Test fun trimRequestUserId() = validationRequestUserIdTrim(command, processor)
    @Test fun emptyRequestUserId() = validationRequestUserIdEmpty(command, processor)
    @Test fun badFormatRequestUserId() = validationRequestUserIdFormat(command, processor)

    @Test fun correctParamValues() = validationParamValuesCorrect(processor)
    @Test fun emptyParamValues() = validationParamValuesEmpty(processor)
}