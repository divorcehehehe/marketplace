package rom.biz.validation

import kotlinx.coroutines.test.runTest
import rom.common.Context
import rom.common.models.ModelFilter
import rom.common.models.Command
import rom.common.models.State
import rom.common.models.WorkMode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

class BizValidationSearchTest: BaseBizValidationTest() {
    override val command = Command.SEARCH

    @Test fun correctSearchString() = validationSearchStringCorrect(processor)
    @Test fun trimSearchString() = validationSearchStringTrim(processor)
    @Test fun tooSmallSearchString() = validationSearchStringTooSmall(processor)
    @Test fun tooLongSearchString() = validationSearchStringTooLong(processor)
}
