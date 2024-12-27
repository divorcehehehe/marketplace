package rom.biz.validation

import rom.biz.ModelProcessor
import rom.common.Context
import rom.common.models.*
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

fun validationSearchRequestUserIdCorrect(processor: ModelProcessor) = runBizTest {
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
    assertEquals("user", ctx.modelFilterValidated.requestUserId.asString())
}

fun validationSearchRequestUserIdTrim(processor: ModelProcessor) = runBizTest {
    val ctx = Context(
        command = Command.SEARCH,
        state = State.NONE,
        workMode = WorkMode.TEST,
        modelFilterRequest = ModelFilter(
            requestUserId = UserId(" \n\t user \n\t "),
            searchString = "search",
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(State.FAILING, ctx.state)
    assertEquals("user", ctx.modelFilterValidated.requestUserId.asString())
}

fun validationSearchRequestUserIdEmpty(processor: ModelProcessor) = runBizTest {
    val ctx = Context(
        command = Command.SEARCH,
        state = State.NONE,
        workMode = WorkMode.TEST,
        modelFilterRequest = ModelFilter(
            requestUserId = UserId.NONE,
            searchString = "search",
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(State.FAILING, ctx.state)
    assertEquals("requestUserId", ctx.errors.firstOrNull()?.field)
}

fun validationSearchRequestUserIdFormat(processor: ModelProcessor) = runBizTest {
    val ctx = Context(
        command = Command.SEARCH,
        state = State.NONE,
        workMode = WorkMode.TEST,
        modelFilterRequest = ModelFilter(
            requestUserId = UserId("!@#$%^&*(),.{}"),
            searchString = "search",
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(State.FAILING, ctx.state)
    assertEquals("requestUserId", ctx.errors.firstOrNull()?.field)
}
