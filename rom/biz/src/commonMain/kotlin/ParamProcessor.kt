package rom.biz

import marketplace.cor.rootChain
import marketplace.cor.worker
import rom.biz.general.initStatus
import rom.biz.general.operation
import rom.biz.general.stubs
import rom.biz.stubs.*
import rom.biz.validation.*
import rom.common.models.Param
import rom.common.models.ParamCorSettings
import rom.common.models.Command
import rom.common.models.ModelId
import rom.common.models.ModelLock

class ParamProcessor(
    val corSettings: ParamCorSettings = ParamCorSettings.NONE
) {
    suspend fun exec(param: Param) = businessChain.exec(param.also { it.corSettings = corSettings })
    private val businessChain = rootChain {
        validateParamLineHasContent("Проверка params[${corSettings.paramIndex}].line")
        validateParamPositionHasContent("Проверка params[${corSettings.paramIndex}].position")
        validateParamSeparatorNotEmpty("Проверка params[${corSettings.paramIndex}].separator")
        validateParamNameNotEmpty("Проверка существования params[${corSettings.paramIndex}].name")
        validateParamNameHasContent("Проверка params[${corSettings.paramIndex}].name")
        validateParamUnitsNotEmpty("Проверка params[${corSettings.paramIndex}].units")
        validateParamUnitsHasContent("Проверка params[${corSettings.paramIndex}].units")
        validateParamBoundsSize("Проверка params[${corSettings.paramIndex}].bounds.size")
        validateParamBoundsAsc("Проверка params[${corSettings.paramIndex}].bounds order")
    }.build()
}
