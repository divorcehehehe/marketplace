package rom.biz.validation

import rom.common.models.Command
import kotlin.test.Test

class BizValidationDeleteTest: BaseBizValidationTest() {
    override val command = Command.DELETE

    @Test fun correctId() = validationIdCorrect(command, processor)
    @Test fun trimId() = validationIdTrim(command, processor)
    @Test fun emptyId() = validationIdEmpty(command, processor)
    @Test fun badFormatId() = validationIdFormat(command, processor)

    @Test fun correctRequestUserId() = validationRequestUserIdCorrect(command, processor)
    @Test fun trimRequestUserId() = validationRequestUserIdTrim(command, processor)
    @Test fun emptyRequestUserId() = validationRequestUserIdEmpty(command, processor)
    @Test fun badFormatRequestUserId() = validationRequestUserIdFormat(command, processor)

    @Test fun correctLock() = validationLockCorrect(command, processor)
    @Test fun trimLock() = validationLockTrim(command, processor)
    @Test fun emptyLock() = validationLockEmpty(command, processor)
    @Test fun badFormatLock() = validationLockFormat(command, processor)

}
