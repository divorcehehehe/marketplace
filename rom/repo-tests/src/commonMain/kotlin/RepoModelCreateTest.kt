package rom.backend.repo.tests

import rom.common.models.*
import rom.common.repo.DbModelRequest
import rom.common.repo.DbModelResponseOk
import rom.repo.common.ModelRepoInitialized
import kotlin.test.*

abstract class RepoModelCreateTest {
    abstract val repo: ModelRepoInitialized
    protected open val lockNew = ModelLock("20000000-0000-0000-0000-000000000002")

    private val createObj = Model(
        ownerId = UserId("owner-id"),
        requestUserId = UserId("owner-id"),
        name = "обтекание крыла",
        macroPath = "путь/к/макросу",
        solverPath = "путь/к/солверу",
        params = mutableListOf(
            Param(
                line = 1,
                position = 2,
                separator = "=",
                name = "Скорость",
                units = "м/с",
                bounds = mutableListOf(100.0, 200.0),
            )
        ),
        sampling = Sampling.ADAPTIVE_SAMPLING,
        visibility = Visibility.VISIBLE_PUBLIC,
    )

    @Test
    fun createSuccess() = runRepoTest {
        val result = repo.createModel(DbModelRequest(createObj))
        assertIs<DbModelResponseOk>(result)
        assertNotEquals(ModelId.NONE, result.data.id)
        assertEquals(lockNew, result.data.lock)
        assertEquals(createObj.ownerId,    result.data.ownerId)
        assertEquals(createObj.name,       result.data.name)
        assertEquals(createObj.macroPath,  result.data.macroPath)
        assertEquals(createObj.solverPath, result.data.solverPath)
        assertEquals(createObj.params[0].line,      result.data.params.firstOrNull()?.line)
        assertEquals(createObj.params[0].position,  result.data.params.firstOrNull()?.position)
        assertEquals(createObj.params[0].separator, result.data.params.firstOrNull()?.separator)
        assertEquals(createObj.params[0].name,      result.data.params.firstOrNull()?.name)
        assertEquals(createObj.params[0].units,     result.data.params.firstOrNull()?.units)
        assertEquals(createObj.params[0].bounds,    result.data.params.firstOrNull()?.bounds)
        assertEquals(result.data.id, result.data.params.firstOrNull()?.modelId)
        assertNotEquals(ParamId.NONE, result.data.params.firstOrNull()?.paramId)
    }

    companion object : BaseInitModels("create") {
        override val initObjects: List<Model> = emptyList()
    }
}
