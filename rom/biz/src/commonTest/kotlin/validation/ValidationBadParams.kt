package rom.biz.validation

import rom.biz.ModelProcessor
import rom.common.Context
import rom.common.models.*
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals

private val model = Model(
    id = ModelId("666"),
    lock = ModelLock("123-234-abc-ABC"),
    name = "name 666",
    macroPath = "macro/path/666",
    solverPath = "solver/path/666",
    params = mutableListOf(
        Param(
            line = 1,
            position = 2,
            separator = " = ",
            name = "Temperature",
            units = "DoC",
            bounds = mutableListOf(0.0, 100.0),
        ),
        Param(
            line = 2,
            position = 2,
            separator = " = ",
            name = "Pressure",
            units = "MPa",
            bounds = mutableListOf(1e6, 1e8),
        ),
    ),
    visibility = Visibility.VISIBLE_PUBLIC,
)

fun validationParamsCorrect(command: Command, processor: ModelProcessor) = runBizTest {
    val ctx = Context(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        modelRequest = model.copy(),
    )
    processor.exec(ctx)
    assertEquals(0, ctx.errors.size)
    assertNotEquals(State.FAILING, ctx.state)
    assertEquals(model.params, ctx.modelValidated.params)
}

fun validationParamsEmpty(command: Command, processor: ModelProcessor) = runBizTest {
    val ctx = Context(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        modelRequest = model.copy(params = mutableListOf()),
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(State.FAILING, ctx.state)
    assertEquals("params", ctx.errors.firstOrNull()?.field)
}

fun validationParamsBadLine(command: Command, processor: ModelProcessor) = runBizTest {
    val ctx = Context(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        modelRequest = model.copyAndEditSecondParam { copy(line = -10) },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(State.FAILING, ctx.state)
    assertEquals("params[1].line", ctx.errors.firstOrNull()?.field)
}

fun validationParamsBadPosition(command: Command, processor: ModelProcessor) = runBizTest {
    val ctx = Context(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        modelRequest = model.copyAndEditSecondParam { copy(position = -10) },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(State.FAILING, ctx.state)
    assertEquals("params[1].position", ctx.errors.firstOrNull()?.field)
}

fun validationParamsEmptySeparator(command: Command, processor: ModelProcessor) = runBizTest {
    val ctx = Context(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        modelRequest = model.copyAndEditSecondParam { copy(separator = "") },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(State.FAILING, ctx.state)
    assertEquals("params[1].separator", ctx.errors.firstOrNull()?.field)
}

fun validationParamsEmptyName(command: Command, processor: ModelProcessor) = runBizTest {
    val ctx = Context(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        modelRequest = model.copyAndEditSecondParam { copy(name = "") },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(State.FAILING, ctx.state)
    assertEquals("params[1].name", ctx.errors.firstOrNull()?.field)
}

fun validationParamsBadName(command: Command, processor: ModelProcessor) = runBizTest {
    val ctx = Context(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        modelRequest = model.copyAndEditSecondParam { copy(name = "!@#\$%^&*(),.{}") },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(State.FAILING, ctx.state)
    assertEquals("params[1].name", ctx.errors.firstOrNull()?.field)
}

fun validationParamsEmptyUnits(command: Command, processor: ModelProcessor) = runBizTest {
    val ctx = Context(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        modelRequest = model.copyAndEditSecondParam { copy(units = "") },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(State.FAILING, ctx.state)
    assertEquals("params[1].units", ctx.errors.firstOrNull()?.field)
}

fun validationParamsBadUnits(command: Command, processor: ModelProcessor) = runBizTest {
    val ctx = Context(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        modelRequest = model.copyAndEditSecondParam { copy(units = "!@#\$%^&*(),.{}") },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(State.FAILING, ctx.state)
    assertEquals("params[1].units", ctx.errors.firstOrNull()?.field)
}

fun validationParamsBadBoundsSize(command: Command, processor: ModelProcessor) = runBizTest {
    val ctx = Context(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        modelRequest = model.copyAndEditSecondParam { copy(bounds = mutableListOf(0.0, 1.0, 2.0)) },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(State.FAILING, ctx.state)
    assertEquals("params[1].bounds", ctx.errors.firstOrNull()?.field)
}

fun validationParamsBadBoundsValues(command: Command, processor: ModelProcessor) = runBizTest {
    val ctx = Context(
        command = command,
        state = State.NONE,
        workMode = WorkMode.TEST,
        modelRequest = model.copyAndEditSecondParam { copy(bounds = mutableListOf(1.0, 0.0)) },
    )
    processor.exec(ctx)
    assertEquals(1, ctx.errors.size)
    assertEquals(State.FAILING, ctx.state)
    assertEquals("params[1].bounds", ctx.errors.firstOrNull()?.field)
}

private fun Model.copyAndEditSecondParam(fieldModifier: Param.() -> Param): Model {
    val newParams = params.mapIndexed { index, param ->
        if (index == 1) param.copy().fieldModifier() else param.copy()
    }.toMutableList()

    return this.copy(params = newParams)
}
