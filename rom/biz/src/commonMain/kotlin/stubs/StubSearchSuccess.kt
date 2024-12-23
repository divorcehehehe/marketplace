package rom.biz.stubs

import marketplace.cor.ICorChainDsl
import marketplace.cor.worker
import marketplace.logging.common.LogLevel
import rom.common.Context
import rom.common.CorSettings
import rom.common.models.*
import rom.common.stubs.Stubs
import rom.stubs.ModelStub

fun ICorChainDsl<Context>.stubSearchSuccess(title: String, corSettings: CorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для поиска моделей
    """.trimIndent()
    on { stubCase == Stubs.SUCCESS && state == State.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubSearchSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = State.FINISHING
            modelsResponse.addAll(ModelStub.prepareSearchList(modelFilterRequest.searchString))
        }
    }
}
