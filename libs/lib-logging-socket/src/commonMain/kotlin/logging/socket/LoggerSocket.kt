package marketplace.logging.socket

import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import marketplace.logging.common.ILogWrapper
import kotlin.reflect.KClass

data class SocketLoggerSettings(
    val host: String = "127.0.0.1",
    val port: Int = 9002,
    val emitToStdout: Boolean = true,
    val bufferSize: Int = 16,
    val overflowPolicy: BufferOverflow = BufferOverflow.SUSPEND,
    val scope: CoroutineScope = CoroutineScope(Dispatchers.Default + CoroutineName("Logging")),
)

@OptIn(ExperimentalStdlibApi::class)
@Suppress("unused")
fun mpLoggerSocket(
    loggerId: String,
    settings: SocketLoggerSettings = SocketLoggerSettings()
): ILogWrapper = LoggerWrapperSocket(
    loggerId = loggerId,
    host = settings.host,
    port = settings.port,
    emitToStdout = settings.emitToStdout,
    bufferSize = settings.bufferSize,
    overflowPolicy = settings.overflowPolicy,
    scope = settings.scope,
)

@Suppress("unused")
fun mpLoggerSocket(cls: KClass<*>, settings: SocketLoggerSettings = SocketLoggerSettings()): ILogWrapper = mpLoggerSocket(
    loggerId = cls.qualifiedName ?: "",
    settings = settings,
)
