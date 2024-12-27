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
        )
    ),
    visibility = Visibility.VISIBLE_PUBLIC,
    usVector = arrayOf(   1.0, 2.0, 3.0),
    vtVector = arrayOf(   4.0, 5.0, 6.0),
    paramValues = arrayOf(7.0),
)

fun validationIdCorrect(command: Command, processor: ModelProcessor) = runBizTest {
    val ctx = Context(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        modelRequest = model.copy(),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(State.FAILING, ctx.state)
    assertEquals("666", ctx.modelValidated.id.asString())
}

fun validationIdTrim(command: Command, processor: ModelProcessor) = runBizTest {
    val ctx = Context(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        modelRequest = model.copy(
            id = ModelId(" \n\t 666 \n\t ")
        ),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(State.FAILING, ctx.state)
    assertEquals("666", ctx.modelValidated.id.asString())
}

fun validationIdEmpty(command: Command, processor: ModelProcessor) = runBizTest {
    val ctx = Context(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        modelRequest = model.copy(
            id = ModelId("")
        ),
    )
    processor.exec(ctx)
    assertNotEquals(0, ctx.errors.size) // в предикте есть проверка на пыстые us и vt
    assertEquals(State.FAILING, ctx.state)
    assertEquals("id", ctx.errors.firstOrNull()?.field)
}

fun validationIdFormat(command: Command, processor: ModelProcessor) = runBizTest {
    val ctx = Context(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        modelRequest = model.copy(
            id = ModelId("!@#$%^&*(),.{}"
            )
        ),
    )
    processor.exec(ctx)
    assertNotEquals(0, ctx.errors.size) // в предикте есть проверка на пыстые us и vt
    assertEquals(State.FAILING, ctx.state)
    assertEquals("id", ctx.errors.firstOrNull()?.field)
}