package rom.biz.validation

import rom.biz.ModelProcessor
import rom.common.Context
import rom.common.models.*
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val model = Model(
    id = ModelId("666"),
    lock = ModelLock("123-234-abc-ABC"),
    requestUserId = UserId("user"),
    name = "name 666",
    macroPath = "macro/path/666",
    solverPath = "solver/path/666",
    params = mutableListOf(
        Param(
            line = 666,
            position = 666,
            separator = "666",
            name = "param 666",
            units = "dB",
            bounds = mutableListOf(33.3, 66.6),
        ),
    ),
    visibility = Visibility.VISIBLE_PUBLIC,
    paramValues = arrayOf(1.0, 2.0, 3.0),
)

fun validationLockCorrect(command: Command, processor: ModelProcessor) = runBizTest {
    val ctx = Context(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        modelRequest = model.copy(),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(State.FAILING, ctx.state)
    assertEquals("123-234-abc-ABC", ctx.modelValidated.lock.asString())
}

fun validationLockTrim(command: Command, processor: ModelProcessor) = runBizTest {
    val ctx = Context(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        modelRequest = model.copy(
            lock = ModelLock(" \n\t 123-234-abc-ABC \n\t ")
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(State.FAILING, ctx.state)
    assertEquals("123-234-abc-ABC", ctx.modelValidated.lock.asString())
}

fun validationLockEmpty(command: Command, processor: ModelProcessor) = runBizTest {
    val ctx = Context(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        modelRequest = model.copy(
            lock = ModelLock("")
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(State.FAILING, ctx.state)
    assertEquals("lock", ctx.errors.firstOrNull()?.field)
}

fun validationLockFormat(command: Command, processor: ModelProcessor) = runBizTest {
    val ctx = Context(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        modelRequest = model.copy(
            lock = ModelLock("!@#\$%^&*(),.{}")
        ),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(State.FAILING, ctx.state)
    assertEquals("lock", ctx.errors.firstOrNull()?.field)
}
