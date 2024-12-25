package rom.biz.stub

import kotlinx.coroutines.test.runTest
import rom.biz.ModelProcessor
import rom.common.Context
import rom.common.models.*
import rom.common.stubs.Stubs
import rom.stubs.ModelStub
import kotlin.test.Test
import kotlin.test.assertEquals

class ModelPredictStubTest {

    private val processor = ModelProcessor()
    private val id = ModelId("666")
    private val lock = ModelLock("666")
    private val paramValues = arrayOf(0.5)

    @Test
    fun predict() = runTest {

        val ctx = Context(
            command = Command.PREDICT,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.SUCCESS,
            modelRequest = Model(
                id = id,
                lock = lock,
                paramValues = paramValues,
            ),
        )
        processor.exec(ctx)

        with (ModelStub.get()) {
            assertEquals(ownerId,           ctx.modelResponse.ownerId)
            assertEquals(permissionsClient, ctx.modelResponse.permissionsClient)
            assertEquals(name,              ctx.modelResponse.name)
            assertEquals(macroPath,         ctx.modelResponse.macroPath)
            assertEquals(solverPath,        ctx.modelResponse.solverPath)
            assertEquals(params,            ctx.modelResponse.params)
            assertEquals(sampling,          ctx.modelResponse.sampling)
            assertEquals(visibility,        ctx.modelResponse.visibility)
        }
        assertEquals(id,          ctx.modelResponse.id)
        assertEquals(lock,        ctx.modelResponse.lock)
        assertEquals(paramValues, ctx.modelResponse.paramValues)
    }

    @Test
    fun badId() = runTest {
        val ctx = Context(
            command = Command.PREDICT,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_ID,
            modelRequest = Model(
                lock = lock,
                paramValues = paramValues,
            ),
        )
        processor.exec(ctx)
        assertEquals(Model(),               ctx.modelResponse)
        assertEquals("id",         ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badLock() = runTest {
        val ctx = Context(
            command = Command.PREDICT,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_LOCK,
            modelRequest = Model(
                id = id,
                paramValues = paramValues,
            ),
        )
        processor.exec(ctx)
        assertEquals(Model(),               ctx.modelResponse)
        assertEquals("lock",       ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun cannotRead() = runTest {
        val ctx = Context(
            requestUserId = UserId("hacker"),
            command = Command.PREDICT,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.CANNOT_READ,
            modelRequest = Model(
                id = id,
                lock = lock,
                paramValues = paramValues,
            ),
        )
        processor.exec(ctx)
        assertEquals(Model(),                           ctx.modelResponse)
        assertEquals("permissionsModelClient", ctx.errors.firstOrNull()?.field)
        assertEquals("validation",             ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun cannotUpdate() = runTest {
        val ctx = Context(
            requestUserId = UserId("hacker"),
            command = Command.PREDICT,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.CANNOT_UPDATE,
            modelRequest = Model(
                id = id,
                lock = lock,
                paramValues = paramValues,
            ),
        )
        processor.exec(ctx)
        assertEquals(Model(),                           ctx.modelResponse)
        assertEquals("permissionsModelClient", ctx.errors.firstOrNull()?.field)
        assertEquals("validation",             ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = Context(
            command = Command.PREDICT,
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
            command = Command.PREDICT,
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
