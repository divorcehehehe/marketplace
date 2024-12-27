package rom.biz.stub

import kotlinx.coroutines.test.runTest
import rom.biz.ModelProcessor
import rom.common.Context
import rom.common.models.*
import rom.common.stubs.Stubs
import rom.stubs.ModelStub
import kotlin.test.Test
import kotlin.test.assertEquals

class ModelReadStubTest {

    private val processor = ModelProcessor()
    private val id = ModelId("666")

    @Test
    fun read() = runTest {

        val ctx = Context(
            command = Command.READ,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.SUCCESS,
            modelRequest = Model(
                id = id,
            ),
        )
        processor.exec(ctx)
        with (ModelStub.get()) {
            assertEquals(id,                ctx.modelResponse.id)
            assertEquals(ownerId,           ctx.modelResponse.ownerId)
            assertEquals(name,              ctx.modelResponse.name)
            assertEquals(macroPath,         ctx.modelResponse.macroPath)
            assertEquals(solverPath,        ctx.modelResponse.solverPath)
            assertEquals(params,            ctx.modelResponse.params)
            assertEquals(sampling,          ctx.modelResponse.sampling)
            assertEquals(visibility,        ctx.modelResponse.visibility)
        }
    }

    @Test
    fun badId() = runTest {
        val ctx = Context(
            command = Command.READ,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_ID,
            modelRequest = Model(),
        )
        processor.exec(ctx)
        assertEquals(Model(),               ctx.modelResponse)
        assertEquals("id",         ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = Context(
            command = Command.READ,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.DB_ERROR,
            modelRequest = Model(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(Model(),             ctx.modelResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = Context(
            command = Command.READ,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.NONE,
            modelRequest = Model(
                id = id,
            ),
        )
        processor.exec(ctx)
        assertEquals(Model(),         ctx.modelResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
