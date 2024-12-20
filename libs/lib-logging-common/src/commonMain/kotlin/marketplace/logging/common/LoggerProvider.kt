package marketplace.logging.common

import kotlin.reflect.KClass
import kotlin.reflect.KFunction

class LoggerProvider(
    private val provider: (String) -> ILogWrapper = { ILogWrapper.DEFAULT }
) {
    fun logger(loggerId: String): ILogWrapper = provider(loggerId)

    fun logger(clazz: KClass<*>): ILogWrapper = provider(clazz.qualifiedName ?: clazz.simpleName ?: "(unknown)")

    fun logger(function: KFunction<*>): ILogWrapper = provider(function.name)
}
