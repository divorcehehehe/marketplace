package rom.backend.repo.tests

import rom.common.models.*
import rom.common.repo.DbModelIdRequest
import rom.common.repo.DbModelResponseErr
import rom.common.repo.DbModelResponseOk
import rom.repo.common.ModelRepoInitialized
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotEquals


abstract class RepoModelReadTest {
    abstract val repo: ModelRepoInitialized
    protected open val readSucc = initObjects[0]
    protected open val lockNew = ModelLock("20000000-0000-0000-0000-000000000002")

    @Test
    fun readSuccess() = runRepoTest {
        val result = repo.readModel(DbModelIdRequest(readSucc))

        assertIs<DbModelResponseOk>(result)
        assertNotEquals(ModelId.NONE, result.data.id)
        assertEquals(lockNew, result.data.lock)
        assertEquals(readSucc.ownerId,    result.data.ownerId)
        assertEquals(readSucc.name,       result.data.name)
        assertEquals(readSucc.macroPath,  result.data.macroPath)
        assertEquals(readSucc.solverPath, result.data.solverPath)
        assertEquals(readSucc.params[0].line,      result.data.params.firstOrNull()?.line)
        assertEquals(readSucc.params[0].position,  result.data.params.firstOrNull()?.position)
        assertEquals(readSucc.params[0].separator, result.data.params.firstOrNull()?.separator)
        assertEquals(readSucc.params[0].name,      result.data.params.firstOrNull()?.name)
        assertEquals(readSucc.params[0].units,     result.data.params.firstOrNull()?.units)
        assertEquals(readSucc.params[0].bounds,    result.data.params.firstOrNull()?.bounds)
        assertEquals(result.data.id, result.data.params.firstOrNull()?.modelId)
        assertNotEquals(ParamId.NONE, result.data.params.firstOrNull()?.paramId)
    }

    @Test
    fun readIdNotFound() = runRepoTest {
        val result = repo.readModel(DbModelIdRequest(notFoundId, readSucc.ownerId))

        assertIs<DbModelResponseErr>(result)
        assertEquals("id", result.errors.firstOrNull()?.field)
    }

    @Test
    fun readBadId() = runRepoTest {
        val result = repo.readModel(DbModelIdRequest(ModelId.NONE, UserId("owner-123")))

        assertIs<DbModelResponseErr>(result)
        assertEquals("id", result.errors.firstOrNull()?.field)
    }

    @Test
    fun readBadRequestUserId() = runRepoTest {
        val result = repo.readModel(DbModelIdRequest(ModelId("model-repo-read"), UserId.NONE))

        assertIs<DbModelResponseErr>(result)
        assertEquals("requestUserId", result.errors.firstOrNull()?.field)
    }

    @Test
    fun canNotRead() = runRepoTest {
        val result = repo.readModel(DbModelIdRequest(readSucc.id, UserId(
            "██▅▇██▇▆▅▄▄▄▇ <— Saddam Hussein"
        ), readSucc.lock))

        assertIs<DbModelResponseErr>(result)
        assertEquals("visibility", result.errors.firstOrNull()?.field)
    }

    companion object : BaseInitModels("read") {
        override val initObjects: List<Model> = listOf(
            createInitTestModel("read")
        )

        val notFoundId = ModelId("model-repo-read-notFound")
    }
}
