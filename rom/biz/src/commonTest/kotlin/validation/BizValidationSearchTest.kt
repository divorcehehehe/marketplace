package rom.biz.validation

import rom.common.models.Command
import kotlin.test.Test

class BizValidationSearchTest: BaseBizValidationTest() {
    override val command = Command.SEARCH

    @Test fun correctSearchRequestUserId() = validationSearchRequestUserIdCorrect(processor)
    @Test fun trimSearchRequestUserId() = validationSearchRequestUserIdTrim(processor)
    @Test fun emptySearchRequestUserId() = validationSearchRequestUserIdEmpty(processor)
    @Test fun badFormatSearchRequestUserId() = validationSearchRequestUserIdFormat(processor)

    @Test fun correctSearchString() = validationSearchStringCorrect(processor)
    @Test fun trimSearchString() = validationSearchStringTrim(processor)
    @Test fun tooSmallSearchString() = validationSearchStringTooSmall(processor)
    @Test fun tooLongSearchString() = validationSearchStringTooLong(processor)
}
