package rom.biz.stubs

import marketplace.cor.ICorChainDsl
import marketplace.cor.worker
import marketplace.logging.common.LogLevel
import rom.common.Context
import rom.common.CorSettings
import rom.common.models.ModelId
import rom.common.models.ModelLock
import rom.common.models.State
import rom.common.stubs.Stubs
import rom.stubs.ModelStub

fun ICorChainDsl<Context>.stubPredictSuccess(title: String, corSettings: CorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для предсказания модели
    """.trimIndent()
    on { stubCase == Stubs.SUCCESS && state == State.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubPredictSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = State.FINISHING
            val stub = ModelStub.prepareResult {
                modelRequest.id.takeIf { it != ModelId.NONE }?.also { this.id = it }
                modelRequest.lock.takeIf { it != ModelLock.NONE }?.also { this.lock = it }
                modelRequest.paramValues.takeIf { it.isNotEmpty() }?.also { this.paramValues = it }
                this.field = arrayOf(1.0, 2.0, 3.0, 4.0, 5.0)
            }
            modelResponse = stub
        }
    }
}
