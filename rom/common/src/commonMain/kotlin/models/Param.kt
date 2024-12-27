package rom.common.models

data class Param(
    var corSettings: ParamCorSettings = ParamCorSettings(),
    var line: Int = 0,
    var position: Int = 0,
    var separator: String = "",
    var name: String = "",
    var units: String = "",
    val bounds: MutableList<Double> = mutableListOf(),
    var paramId: ParamId = ParamId.NONE,
    var modelId: ModelId = ModelId.NONE,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Param

        if (line != other.line) return false
        if (position != other.position) return false
        if (separator != other.separator) return false
        if (name != other.name) return false
        if (units != other.units) return false
        if (bounds != other.bounds) return false
        if (paramId != other.paramId) return false
        if (modelId != other.modelId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = line
        result = 31 * result + position
        result = 31 * result + separator.hashCode()
        result = 31 * result + name.hashCode()
        result = 31 * result + units.hashCode()
        result = 31 * result + bounds.hashCode()
        result = 31 * result + paramId.hashCode()
        result = 31 * result + modelId.hashCode()
        return result
    }
}
