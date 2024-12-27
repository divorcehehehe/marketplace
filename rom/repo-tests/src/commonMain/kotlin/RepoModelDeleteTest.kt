package rom.backend.repo.tests

import rom.common.models.*
import rom.common.repo.*
import rom.repo.common.ModelRepoInitialized
import kotlin.test.*

abstract class RepoModelDeleteTest {
    abstract val repo: ModelRepoInitialized
    protected open val deleteSucc = initObjects[0]
    protected open val deleteTest = initObjects[1]
    private val notFoundId = ModelId("model-repo-delete-notFound")
    protected open val lockNew = ModelLock("20000000-0000-0000-0000-000000000002")

    @Test
    fun deleteSuccess() = runRepoTest {
        val result = repo.deleteModel(DbModelIdRequest(deleteSucc.id, deleteSucc.ownerId, lock = deleteSucc.lock))

        assertIs<DbModelResponseOk>(result)
        assertNotEquals(ModelId.NONE, result.data.id)
        assertEquals(lockNew, result.data.lock)
        assertEquals(deleteSucc.ownerId,    result.data.ownerId)
        assertEquals(deleteSucc.name,       result.data.name)
        assertEquals(deleteSucc.macroPath,  result.data.macroPath)
        assertEquals(deleteSucc.solverPath, result.data.solverPath)
        assertEquals(deleteSucc.params[0].line,      result.data.params.firstOrNull()?.line)
        assertEquals(deleteSucc.params[0].position,  result.data.params.firstOrNull()?.position)
        assertEquals(deleteSucc.params[0].separator, result.data.params.firstOrNull()?.separator)
        assertEquals(deleteSucc.params[0].name,      result.data.params.firstOrNull()?.name)
        assertEquals(deleteSucc.params[0].units,     result.data.params.firstOrNull()?.units)
        assertEquals(deleteSucc.params[0].bounds,    result.data.params.firstOrNull()?.bounds)
        assertEquals(result.data.id, result.data.params.firstOrNull()?.modelId)
        assertNotEquals(ParamId.NONE, result.data.params.firstOrNull()?.paramId)
    }

    @Test
    fun deleteIdNotFound() = runRepoTest {
        val result = repo.deleteModel(DbModelIdRequest(notFoundId, deleteTest.ownerId, lock = deleteSucc.lock))

        assertIs<DbModelResponseErr>(result)
        assertEquals("id", result.errors.firstOrNull()?.field)
    }

    @Test
    fun deleteBadId() = runRepoTest {
        val result = repo.deleteModel(DbModelIdRequest(ModelId.NONE, deleteTest.ownerId, lock = deleteSucc.lock))

        assertIs<DbModelResponseErr>(result)
        assertEquals("id", result.errors.firstOrNull()?.field)
    }

    @Test
    fun deleteBadRequestUserId() = runRepoTest {
        val result = repo.deleteModel(DbModelIdRequest(deleteTest.id, UserId.NONE, lock = deleteSucc.lock))

        assertIs<DbModelResponseErr>(result)
        assertEquals("requestUserId", result.errors.firstOrNull()?.field)
    }

    @Test
    fun canNotDelete() = runRepoTest {
        val result = repo.deleteModel(DbModelIdRequest(deleteTest.id, UserId(
            "██▅▇██▇▆▅▄▄▄▇ <— Saddam Hussein"
        ), deleteSucc.lock))

        assertIs<DbModelResponseErr>(result)
        assertEquals("visibility", result.errors.firstOrNull()?.field)
    }

    @Test
    fun deleteConcurrency() = runRepoTest {
        val result = repo.deleteModel(DbModelIdRequest(deleteTest.id, deleteTest.ownerId, lock = lockBad))

        assertIs<DbModelResponseErrWithData>(result)
        val error = result.errors.find { it.code == "repo-concurrency" }
        assertNotNull(error)
    }

    companion object : BaseInitModels("delete") {
        override val initObjects: List<Model> = listOf(
            createInitTestModel("delete"),
            createInitTestModel("deleteTest"),
        )
    }
}
