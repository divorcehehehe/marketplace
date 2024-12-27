package rom.biz.validation

import rom.biz.ModelProcessor
import rom.common.Context
import rom.common.models.*
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationSearchStringCorrect(processor: ModelProcessor) = runBizTest {
    val ctx = Context(
        command = Command.SEARCH,
        state = State.NONE,
        workMode = WorkMode.TEST,
        modelFilterRequest = ModelFilter(
            requestUserId = UserId("user"),
            searchString = "search",
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(State.FAILING, ctx.state)
    assertEquals("search", ctx.modelFilterValidated.searchString)
}

fun validationSearchStringTrim(processor: ModelProcessor) = runBizTest {
    val ctx = Context(
        command = Command.SEARCH,
        state = State.NONE,
        workMode = WorkMode.TEST,
        modelFilterRequest = ModelFilter(
            requestUserId = UserId("user"),
            searchString = " \n\t search \n\t ",
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(State.FAILING, ctx.state)
    assertEquals("search", ctx.modelFilterValidated.searchString)
}

fun validationSearchStringTooSmall(processor: ModelProcessor) = runBizTest {
    val ctx = Context(
        command = Command.SEARCH,
        state = State.NONE,
        workMode = WorkMode.TEST,
        modelFilterRequest = ModelFilter(
            requestUserId = UserId("user"),
            searchString = "se",
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(State.FAILING, ctx.state)
    assertEquals("searchString", ctx.errors.firstOrNull()?.field)
}

fun validationSearchStringTooLong(processor: ModelProcessor) = runBizTest {
    val ctx = Context(
        command = Command.SEARCH,
        state = State.NONE,
        workMode = WorkMode.TEST,
        modelFilterRequest = ModelFilter(
            requestUserId = UserId("user"),
            searchString = "search".repeat(20),
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(State.FAILING, ctx.state)
    assertEquals("searchString", ctx.errors.firstOrNull()?.field)
}
