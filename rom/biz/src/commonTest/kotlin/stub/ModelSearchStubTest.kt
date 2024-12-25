package rom.biz.stub

import kotlinx.coroutines.test.runTest
import rom.biz.ModelProcessor
import rom.common.Context
import rom.common.models.*
import rom.common.stubs.Stubs
import rom.stubs.ModelStub
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class ModelSearchStubTest {

    private val processor = ModelProcessor()
    private val filter = ModelFilter(searchString = "обтекание")

    @Test
    fun search() = runTest {

        val ctx = Context(
            command = Command.SEARCH,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.SUCCESS,
            modelFilterRequest = filter,
        )
        processor.exec(ctx)
        val first = ctx.modelsResponse.firstOrNull() ?: fail("Empty response list")
        assertTrue(first.name.contains(filter.searchString))
        with (ModelStub.prepareSearchList(filter.searchString)[0]) {
            assertEquals(id,                first.id)
            assertEquals(ownerId,           first.ownerId)
            assertEquals(permissionsClient, first.permissionsClient)
            assertEquals(name,              first.name)
            assertEquals(macroPath,         first.macroPath)
            assertEquals(solverPath,        first.solverPath)
            assertEquals(params,            first.params)
            assertEquals(sampling,          first.sampling)
            assertEquals(visibility,        first.visibility)
        }
    }

    @Test
    fun badSearchString() = runTest {
        val ctx = Context(
            command = Command.SEARCH,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.BAD_SEARCH_STRING,
            modelFilterRequest = ModelFilter(),
        )
        processor.exec(ctx)
        assertEquals(Model(),               ctx.modelResponse)
        assertEquals("searchString",         ctx.errors.firstOrNull()?.field)
        assertEquals("validation", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun cannotRead() = runTest {
        val ctx = Context(
            requestUserId = UserId("hacker"),
            command = Command.SEARCH,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.CANNOT_READ,
            modelFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(Model(),                           ctx.modelResponse)
        assertEquals("permissionsModelClient", ctx.errors.firstOrNull()?.field)
        assertEquals("validation",             ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun databaseError() = runTest {
        val ctx = Context(
            command = Command.SEARCH,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.DB_ERROR,
            modelFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(Model(),             ctx.modelResponse)
        assertEquals("internal", ctx.errors.firstOrNull()?.group)
    }

    @Test
    fun badNoCase() = runTest {
        val ctx = Context(
            command = Command.SEARCH,
            state = State.NONE,
            workMode = WorkMode.STUB,
            stubCase = Stubs.NONE,
            modelFilterRequest = filter,
        )
        processor.exec(ctx)
        assertEquals(Model(),         ctx.modelResponse)
        assertEquals("stub", ctx.errors.firstOrNull()?.field)
    }
}
