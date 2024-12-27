package rom.backend.repo.tests

import rom.common.models.*
import rom.common.repo.*
import rom.repo.common.ModelRepoInitialized
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class RepoModelUpdateTest {
    abstract val repo: ModelRepoInitialized
    protected open val updateSucc = initObjects[0]
    protected open val updateTest = initObjects[1]
    protected val lockNew = ModelLock("20000000-0000-0000-0000-000000000002")

    private val reqUpdateSucc by lazy {
        Model(
            id = updateSucc.id,
            requestUserId = UserId("owner-123"),
            lock = initObjects.first().lock,
            name = "updated model",
            macroPath = "новый/путь/к/макросу",
            solverPath = "новый/путь/к/солверу",
            params = mutableListOf(
                Param(
                    line = 30,
                    position = 31,
                    separator = "equals",
                    name = "Температура",
                    units = "DoC",
                    bounds = mutableListOf(500.0, 600.0),
                )
            ),
            sampling = Sampling.LATIN_HYPER_CUBE,
            visibility = Visibility.VISIBLE_PUBLIC,
        )
    }

    private val reqUpdateConc by lazy {
        Model(
            id = updateTest.id,
            requestUserId = UserId("owner-123"),
            lock = updateTest.lock,
            name = "updated model",
            macroPath = "новый/путь/к/макросу",
            solverPath = "новый/путь/к/солверу",
            params = mutableListOf(
                Param(
                    line = 30,
                    position = 31,
                    separator = "equals",
                    name = "Температура",
                    units = "DoC",
                    bounds = mutableListOf(500.0, 600.0),
                )
            ),
            sampling = Sampling.LATIN_HYPER_CUBE,
            visibility = Visibility.VISIBLE_PUBLIC,
        )
    }

    @Test
    fun updateSuccess() = runRepoTest {
        val result = repo.updateModel(DbModelRequest(reqUpdateSucc))
        assertIs<DbModelResponseOk>(result)
        assertEquals(reqUpdateSucc.id,         result.data.id)
        assertEquals("owner-123",     result.data.ownerId.asString())
        assertEquals(reqUpdateSucc.name,       result.data.name)
        assertEquals(reqUpdateSucc.macroPath,  result.data.macroPath)
        assertEquals(reqUpdateSucc.solverPath, result.data.solverPath)
        assertEquals(reqUpdateSucc.params[0].line,      result.data.params.firstOrNull()?.line)
        assertEquals(reqUpdateSucc.params[0].position,  result.data.params.firstOrNull()?.position)
        assertEquals(reqUpdateSucc.params[0].separator, result.data.params.firstOrNull()?.separator)
        assertEquals(reqUpdateSucc.params[0].name,      result.data.params.firstOrNull()?.name)
        assertEquals(reqUpdateSucc.params[0].units,     result.data.params.firstOrNull()?.units)
        assertEquals(reqUpdateSucc.params[0].bounds,    result.data.params.firstOrNull()?.bounds)
        assertEquals(lockNew, result.data.lock)
    }

    @Test
    fun updateBadId() = runRepoTest {
        val result = repo.updateModel(DbModelRequest(reqUpdateConc.copy(
            id = ModelId.NONE
        )))
        assertIs<DbModelResponseErr>(result)
        assertEquals("id", result.errors.firstOrNull()?.field)
    }

    @Test
    fun updateBadRequestUserId() = runRepoTest {
        val result = repo.updateModel(DbModelRequest(reqUpdateConc.copy(
            requestUserId = UserId.NONE
        )))
        assertIs<DbModelResponseErr>(result)
        assertEquals("requestUserId", result.errors.firstOrNull()?.field)
    }

    @Test
    fun updateBadVisibility() = runRepoTest {
        val result = repo.updateModel(DbModelRequest(reqUpdateConc.copy(
            requestUserId = UserId("Pussy destroyer")
        )))
        assertIs<DbModelResponseErr>(result)
        assertEquals("visibility", result.errors.firstOrNull()?.field)
    }

    @Test
    fun updateNotFound() = runRepoTest {
        val result = repo.updateModel(DbModelRequest(reqUpdateConc.copy(
            id = ModelId("wrong-id")
        )))
        assertIs<DbModelResponseErr>(result)
        assertEquals("id", result.errors.firstOrNull()?.field)
    }

    @Test
    fun updateConcurrencyError() = runRepoTest {
        val result = repo.updateModel(DbModelRequest(reqUpdateConc.copy(lock = ModelLock("badLock"))))
        assertIs<DbModelResponseErrWithData>(result)
        assertEquals("lock", result.errors.firstOrNull()?.field)
        assertEquals(updateTest.id,         result.data.id)
        assertEquals(updateTest.name,       result.data.name)
        assertEquals(updateTest.ownerId,    result.data.ownerId)
        assertEquals(updateTest.macroPath,  result.data.macroPath)
        assertEquals(updateTest.solverPath, result.data.solverPath)
        assertEquals(updateTest.params[0].line,      result.data.params.firstOrNull()?.line)
        assertEquals(updateTest.params[0].position,  result.data.params.firstOrNull()?.position)
        assertEquals(updateTest.params[0].separator, result.data.params.firstOrNull()?.separator)
        assertEquals(updateTest.params[0].name,      result.data.params.firstOrNull()?.name)
        assertEquals(updateTest.params[0].units,     result.data.params.firstOrNull()?.units)
        assertEquals(updateTest.params[0].bounds,    result.data.params.firstOrNull()?.bounds)
    }

    companion object : BaseInitModels("update") {
        override val initObjects: List<Model> = listOf(
            createInitTestModel("update"),
            createInitTestModel("updateTest"),
        )
    }
}
