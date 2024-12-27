package rom.repo.inmemory

import rom.common.models.*

data class ParamEntity(
    val line: Int? = null,
    val position: Int? = null,
    val separator: String? = null,
    val name: String? = null,
    val units: String? = null,
    val bounds: List<Double>? = null,
    val paramId: String? = null,
    val modelId: String? = null,
) {
    constructor(param: Param): this(
        line = param.line.takeIf { it != 0 },
        position = param.position.takeIf { it != 0 },
        separator = param.separator.takeIf { it.isNotBlank() },
        name = param.name.takeIf { it.isNotBlank() },
        units = param.units.takeIf { it.isNotBlank() },
        bounds = param.bounds.takeIf { it.isNotEmpty() },
        paramId = param.paramId.asString().takeIf { it.isNotBlank() },
        modelId = param.modelId.asString().takeIf { it.isNotBlank() },
    )

    fun toInternal() = Param(
        line = line ?: 0,
        position = position ?: 0,
        separator = separator ?: "",
        name = name ?: "",
        units = units ?: "",
        bounds = bounds?.toMutableList() ?: mutableListOf(),
        paramId = paramId?.let { ParamId(it) } ?: ParamId.NONE,
        modelId = modelId?.let { ModelId(it) } ?: ModelId.NONE,
    )
}
