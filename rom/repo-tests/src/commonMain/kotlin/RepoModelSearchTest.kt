package rom.backend.repo.tests

import rom.common.models.Model
import rom.common.models.UserId
import rom.common.models.Visibility
import rom.common.repo.DbModelFilterRequest
import rom.common.repo.DbModelsResponseErr
import rom.common.repo.DbModelsResponseOk
import rom.repo.common.ModelRepoInitialized
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs


abstract class RepoModelSearchTest {
    abstract val repo: ModelRepoInitialized

    protected open val initializedObjects: List<Model> = initObjects

    @Test
    fun badRequestUserId() = runRepoTest {
        val result = repo.searchModel(DbModelFilterRequest(nameFilter = "model", requestUserId = UserId.NONE))
        assertIs<DbModelsResponseErr>(result)
        assertEquals("requestUserId", result.errors.firstOrNull()?.field)
    }

    @Test
    fun searchByName() = runRepoTest {
        val result = repo.searchModel(DbModelFilterRequest(nameFilter = "model", requestUserId = ownerId))
        assertIs<DbModelsResponseOk>(result)
        val expected = listOf(
            initializedObjects[0],
            initializedObjects[1],
            initializedObjects[3],
        ).sortedBy { it.name }
        assertEquals(expected.map { it.name }, result.data.sortedBy { it.name }.map { it.name })
    }

    companion object: BaseInitModels("search") {

        val ownerId = UserId("owner-124")
        override val initObjects: List<Model> = listOf(
            createInitTestModel("model1", visibility = Visibility.VISIBLE_PUBLIC),
            createInitTestModel("model2", visibility = Visibility.VISIBLE_PUBLIC, ownerId = ownerId),
            createInitTestModel("model3", visibility = Visibility.VISIBLE_TO_OWNER),
            createInitTestModel("model4", visibility = Visibility.VISIBLE_TO_OWNER, ownerId = ownerId),
        )
    }
}
