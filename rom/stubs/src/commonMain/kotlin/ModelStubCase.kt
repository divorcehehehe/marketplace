package rom.stubs

import rom.common.models.*

object ModelStubCase {
    val MODEL_STUB_CASE: Model
        get() = Model(
            id = ModelId("666"),
            lock = ModelLock("123-234-abc-ABC"),
            ownerId = UserId("user"),
            requestUserId = UserId("user"),
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
                )
            ),
            usVector = arrayOf(9.0, 9.0, 9.0),
            vtVector = arrayOf(9.0, 9.0, 9.0),
            sampling = Sampling.ADAPTIVE_SAMPLING,
            visibility = Visibility.VISIBLE_PUBLIC,
        )
}
