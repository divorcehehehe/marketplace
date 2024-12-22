package rom.app.tmp

import kotlinx.coroutines.delay
import kotlinx.datetime.Clock
import rom.api.log.mapper.toLog
import rom.common.Context
import rom.common.models.*
import marketplace.logging.common.LogLevel
import marketplace.logging.jvm.loggerLogback

suspend fun main() {
    val logger = loggerLogback("app-tmp")
    while (true) {
        val ctx = Context(
            command = Command.CREATE,
            state = State.RUNNING,
            workMode = WorkMode.STUB,
            timeStart = Clock.System.now(),
            requestId = RequestId("tmp-request"),
            modelRequest = Model(
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
                    ),
                ),
                sampling = Sampling.ADAPTIVE_SAMPLING,
                visibility = Visibility.VISIBLE_PUBLIC,
            ),
            modelResponse = Model(
                id = ModelId("model_id"),
                ownerId = UserId("user_id"),
                permissionsClient = mutableSetOf(
                    ModelPermissionClient.READ,
                    ModelPermissionClient.UPDATE
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
                    ),
                ),
                sampling = Sampling.ADAPTIVE_SAMPLING,
                visibility = Visibility.VISIBLE_PUBLIC,
            ),
            errors = mutableListOf(
                ROMError(
                    code = "tmp-error",
                    group = "tmp",
                    field = "none",
                    message = "tmp error message",
                    level = LogLevel.INFO,
                    exception = Exception("some exception"),
                ),
            ),
        )
        logger.info(
            msg = "tmp log string",
            data = ctx.toLog("tmp-app-logg"),
        )
        delay(500)
    }
}
