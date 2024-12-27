package rom.backend.repo.tests

import kotlinx.coroutines.test.runTest
import rom.common.models.Model
import rom.common.models.UserId
import rom.common.repo.*
import rom.stubs.ModelStub
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class ModelRepositoryMockTest {
    private val repo = ModelRepositoryMock(
        invokeCreateModel = { DbModelResponseOk(ModelStub.prepareResult { name = "create" }) },
        invokeReadModel = { DbModelResponseOk(ModelStub.prepareResult { name = "read" }) },
        invokeUpdateModel = { DbModelResponseOk(ModelStub.prepareResult { name = "update" }) },
        invokeDeleteModel = { DbModelResponseOk(ModelStub.prepareResult { name = "delete" }) },
        invokeSearchModel = { DbModelsResponseOk(listOf(ModelStub.prepareResult { name = "search" })) },
    )

    @Test
    fun mockCreate() = runTest {
        val result = repo.createModel(DbModelRequest(Model()))
        assertIs<DbModelResponseOk>(result)
        assertEquals("create", result.data.name)
    }

    @Test
    fun mockRead() = runTest {
        val result = repo.readModel(DbModelIdRequest(Model()))
        assertIs<DbModelResponseOk>(result)
        assertEquals("read", result.data.name)
    }

    @Test
    fun mockUpdate() = runTest {
        val result = repo.updateModel(DbModelRequest(Model()))
        assertIs<DbModelResponseOk>(result)
        assertEquals("update", result.data.name)
    }

    @Test
    fun mockDelete() = runTest {
        val result = repo.deleteModel(DbModelIdRequest(Model()))
        assertIs<DbModelResponseOk>(result)
        assertEquals("delete", result.data.name)
    }

    @Test
    fun mockSearch() = runTest {
        val result = repo.searchModel(DbModelFilterRequest("", UserId("скажи скорая")))
        assertIs<DbModelsResponseOk>(result)
        assertEquals("search", result.data.first().name)
    }

}
