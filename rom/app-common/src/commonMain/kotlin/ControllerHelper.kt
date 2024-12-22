package rom.app.common

import kotlinx.datetime.Clock
import rom.api.log.mapper.toLog
import rom.common.Context
import rom.common.helpers.asError
import rom.common.models.State
import kotlin.reflect.KClass

suspend inline fun <T> IAppSettings.controllerHelper(
    crossinline getRequest: suspend Context.() -> Unit,
    crossinline toResponse: suspend Context.() -> T,
    clazz: KClass<*>,
    logId: String,
): T {
    val logger = corSettings.loggerProvider.logger(clazz)
    val ctx = Context(
        timeStart = Clock.System.now(),
    )
    return try {
        ctx.getRequest()
        logger.info(
            msg = "Request $logId started for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        processor.exec(ctx)
        logger.info(
            msg = "Request $logId processed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        ctx.toResponse()
    } catch (e: Throwable) {
        logger.error(
            msg = "Request $logId failed for ${clazz.simpleName}",
            marker = "BIZ",
            data = ctx.toLog(logId)
        )
        ctx.state = State.FAILING
        ctx.errors.add(e.asError())
        processor.exec(ctx)
        ctx.toResponse()
    }
}
