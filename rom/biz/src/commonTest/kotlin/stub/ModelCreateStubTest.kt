package rom.biz.stub

import kotlinx.coroutines.test.runTest
import rom.biz.ModelProcessor
import rom.common.Context
import rom.common.models.*
import rom.common.stubs.Stubs
import kotlin.test.Test
import kotlin.test.assertEquals

class ModelCreateStubTest {

    private val processor = ModelProcessor()
    private val name = "name 666"
    private val macroPath = "macro path 666"
    private val solverPath = "solver path 666"
    private val params = mutableListOf(
        Param(
            line = 666,
            position = 666,
            separator = "666",
            name = "param 666",
            units = "dB",
            bounds = mutableListOf(66.6, 66.6),
        )
    )
    private val sampling = Sampling.LATIN_HYPER_CUBE
    private val visibility = Visibility.VISIBLE_PUBLIC
    private val modelRequest = Model(
        name = name,
        macroPath = macroPath,
        solverPath = solverPath,
        params = params,
        sampling = sampling,
        visibility = visibility,
    )

    @Test
    fun create() = runTest {
        val ctx = Context(
            command = Command.CREATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.SUCCESS,
            modelRequest = modelRequest,
        )
        processor.exec(ctx)
        assertEquals(name,           ctx.modelResponse.name)
        assertEquals(macroPath,      ctx.modelResponse.macroPath)
        assertEquals(solverPath,     ctx.modelResponse.solverPath)
        assertEquals(params.first(), ctx.modelResponse.params.firstOrNull())
        assertEquals(sampling,       ctx.modelResponse.sampling)
        assertEquals(visibility,     ctx.modelResponse.visibility)
    }

    @Test
    fun badName() = runTest {
        val ctx = Context(
            command = Command.CREATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_NAME,
            modelRequest = modelRequest.copy(name = ""),
        )
        processor.exec(ctx)
        assertEquals(Model(),               ctx.modelResponse)
        assertEquals("name",       ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badMacroPath() = runTest {
        val ctx = Context(
            command = Command.CREATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_MACRO_PATH,
            modelRequest = modelRequest.copy(macroPath = ""),
        )
        processor.exec(ctx)
        assertEquals(Model(),               ctx.modelResponse)
        assertEquals("macroPath",  ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badSolverPath() = runTest {
        val ctx = Context(
            command = Command.CREATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_SOLVER_PATH,
            modelRequest = modelRequest.copy(solverPath = ""),
        )
        processor.exec(ctx)
        assertEquals(Model(),               ctx.modelResponse)
        assertEquals("solverPath", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badParamLine() = runTest {
        val ctx = Context(
            command = Command.CREATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_PARAM_LINE,
            modelRequest = modelRequest.copyAndEditFirstParam {copy(line = 0)},
        )
        processor.exec(ctx)
        assertEquals(Model(),               ctx.modelResponse)
        assertEquals("param.line", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badParamPosition() = runTest {
        val ctx = Context(
            command = Command.CREATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_PARAM_POSITION,
            modelRequest = modelRequest.copyAndEditFirstParam {copy(position = 0)},
        )
        processor.exec(ctx)
        assertEquals(Model(),                   ctx.modelResponse)
        assertEquals("param.position", ctx.errors.firstOrNull()?.field)
        assertEquals("validation",     ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badParamSeparator() = runTest {
        val ctx = Context(
            command = Command.CREATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_PARAM_SEPARATOR,
            modelRequest = modelRequest.copyAndEditFirstParam {copy(separator = "")},
        )
        processor.exec(ctx)
        assertEquals(Model(),                    ctx.modelResponse)
        assertEquals("param.separator", ctx.errors.firstOrNull()?.field)
        assertEquals("validation",      ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badParamName() = runTest {
        val ctx = Context(
            command = Command.CREATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_PARAM_NAME,
            modelRequest = modelRequest.copyAndEditFirstParam {copy(name = "")},
        )
        processor.exec(ctx)
        assertEquals(Model(),               ctx.modelResponse)
        assertEquals("param.name", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badParamUnits() = runTest {
        val ctx = Context(
            command = Command.CREATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_PARAM_UNITS,
            modelRequest = modelRequest.copyAndEditFirstParam {copy(units = "")},
        )
        processor.exec(ctx)
        assertEquals(Model(),                ctx.modelResponse)
        assertEquals("param.units", ctx.errors.firstOrNull()?.field)
        assertEquals("validation",  ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badParamBounds() = runTest {
        val ctx = Context(
            command = Command.CREATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_PARAM_BOUNDS,
            modelRequest = modelRequest.copyAndEditFirstParam {copy(bounds = mutableListOf())},
        )
        processor.exec(ctx)
        assertEquals(Model(),                 ctx.modelResponse)
        assertEquals("param.bounds", ctx.errors.firstOrNull()?.field)
        assertEquals("validation",   ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badSampling() = runTest {
        val ctx = Context(
            command = Command.CREATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_SAMPLING,
            modelRequest = modelRequest.copy(sampling = Sampling.NONE),
        )
        processor.exec(ctx)
        assertEquals(Model(),               ctx.modelResponse)
        assertEquals("sampling",   ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badVisibility() = runTest {
        val ctx = Context(
            command = Command.CREATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_VISIBILITY,
            modelRequest = modelRequest.copy(visibility = Visibility.NONE),
        )
        processor.exec(ctx)
        assertEquals(Model(),               ctx.modelResponse)
        assertEquals("visibility", ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = Context(
            command = Command.CREATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.DB_ERROR,
            modelRequest = modelRequest,
        )
        processor.exec(ctx)
        assertEquals(Model(),         ctx.modelResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = Context(
            command = Command.CREATE,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.NONE,
            modelRequest = modelRequest,
        )
        processor.exec(ctx)
        assertEquals(Model(),         ctx.modelResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}

private fun Model.copyAndEditFirstParam(fieldModifier: Param.() -> Param): Model {
    val newParams = params.mapIndexed { index, param ->
        if (index == 0) param.copy().fieldModifier() else param.copy()
    }.toMutableList()

    return this.copy(params = newParams)
}
