package rom.biz.stubs

import marketplace.cor.ICorChainDsl
import marketplace.cor.worker
import marketplace.logging.common.LogLevel
import rom.common.Context
import rom.common.CorSettings
import rom.common.models.Param
import rom.common.models.Sampling
import rom.common.models.State
import rom.common.models.Visibility
import rom.common.stubs.Stubs
import rom.stubs.ModelStub

fun ICorChainDsl<Context>.stubCreateSuccess(title: String, corSettings: CorSettings) = worker {
    this.title = title
    this.description = """
        Кейс успеха для создания модели
    """.trimIndent()
    on { stubCase == Stubs.SUCCESS && state == State.RUNNING }
    val logger = corSettings.loggerProvider.logger("stubCreateSuccess")
    handle {
        logger.doWithLogging(id = this.requestId.asString(), LogLevel.DEBUG) {
            state = State.FINISHING
            val stub = ModelStub.prepareResult {
                modelRequest.name.takeIf { it.isNotBlank() }?.also { this.name = it }
                modelRequest.macroPath.takeIf { it.isNotBlank() }?.also { this.macroPath = it }
                modelRequest.solverPath.takeIf { it.isNotBlank() }?.also { this.solverPath = it }
                modelRequest.params.takeIf { it.validate().isNotEmpty() }?.also { this.params = it }
                modelRequest.sampling.takeIf { it != Sampling.NONE }?.also { this.sampling = it }
                modelRequest.visibility.takeIf { it != Visibility.NONE }?.also { this.visibility = it }
            }
            modelResponse = stub
        }
    }
}

private fun MutableList<Param>.validate(): MutableList<Param> = this.filter {
    param -> param.line != 0 &&
        param.position != 0 &&
        param.separator.isNotBlank() &&
        param.name.isNotBlank() &&
        param.units.isNotBlank() &&
        param.bounds.isNotEmpty()
}.toMutableList()
