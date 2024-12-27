package rom.backend.repo.tests

import rom.common.models.*

abstract class BaseInitModels(private val op: String): IInitObjects<Model> {
    open val lockOld: ModelLock = ModelLock("20000000-0000-0000-0000-000000000002")
    open val lockBad: ModelLock = ModelLock("20000000-0000-0000-0000-000000000009")

    fun createInitTestModel(
        suf: String,
        requestUserId: UserId = UserId("owner-123"),
        ownerId: UserId = UserId("owner-123"),
        lock: ModelLock = lockOld,
        visibility: Visibility = Visibility.VISIBLE_TO_OWNER
    ) = Model(
        id = ModelId("model-repo-$suf"),
        ownerId = ownerId,
        requestUserId = requestUserId,
        lock = lock,
        name = "$suf model",
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
                paramId = ParamId(lock.asString()),
                modelId = ModelId("model-repo-$suf"),
            )
        ),
        sampling = Sampling.ADAPTIVE_SAMPLING,
        visibility = visibility,
    )
}
