package rom.app.rabbit

import kotlinx.coroutines.runBlocking
import rom.app.rabbit.config.AppSettings
import rom.app.rabbit.config.RabbitConfig
import rom.app.rabbit.mappers.fromArgs
import rom.common.CorSettings
import marketplace.logging.common.LoggerProvider
import marketplace.logging.jvm.loggerLogback

fun main(vararg args: String) = runBlocking {
    val appSettings = AppSettings(
        rabbit = RabbitConfig.fromArgs(*args),
        corSettings = CorSettings(
            loggerProvider = LoggerProvider { loggerLogback(it) }
        )
    )
    val app = RabbitApp(appSettings = appSettings, this)
    app.start()
}
