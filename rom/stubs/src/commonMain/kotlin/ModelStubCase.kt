package rom.stubs

import rom.common.models.*

object ModelStubCase {
    val MODEL_STUB_CASE: Model
        get() = Model(
            id = ModelId("666"),
            ownerId = UserId("user-1"),
            permissionsClient = mutableSetOf(
                ModelPermissionClient.READ,
                ModelPermissionClient.UPDATE,
                ModelPermissionClient.DELETE,
                ModelPermissionClient.MAKE_VISIBLE_PUBLIC,
                ModelPermissionClient.MAKE_VISIBLE_GROUP,
                ModelPermissionClient.MAKE_VISIBLE_OWNER,
            ),
            name = "Обтекание крыла",
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
                    paramId = ParamId("1"),
                    modelId = ModelId("666"),
                )
            ),
            sampling = Sampling.ADAPTIVE_SAMPLING,
            visibility = Visibility.VISIBLE_PUBLIC,
        )
}
