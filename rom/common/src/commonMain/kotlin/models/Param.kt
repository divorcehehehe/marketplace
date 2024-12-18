package rom.common.models

data class Param(
    var line: Int = 0,
    var position: Int = 0,
    var separator: String = "",
    var name: String = "",
    var units: String = "",
    val bounds: MutableList<Double> = mutableListOf(),
    var paramId: ParamId = ParamId.NONE,
    var modelId: ModelId = ModelId.NONE,
)
