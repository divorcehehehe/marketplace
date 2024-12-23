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

fun ICorChainDsl<Context>.stubTrainSuccess(title: String, corSettings: CorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для обучения модели
    """.trimIndent()
    on { stubCase == Stubs.SUCCESS && state == State.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubTrainSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = State.FINISHING
            val stub = ModelStub.prepareResult {
                modelRequest.id.takeIf { it != ModelId.NONE }?.also { this.id = it }
                modelRequest.lock.takeIf { it != ModelLock.NONE }?.also { this.lock = it }
            }
            modelResponse = stub
        }
    }
}
