package rom.common

import marketplace.logging.common.LoggerProvider

data class CorSettings(
    val loggerProvider: LoggerProvider = LoggerProvider(),
) {
    companion object {
        val NONE = CorSettings()
    }
}
