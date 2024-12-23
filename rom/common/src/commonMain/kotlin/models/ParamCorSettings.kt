package rom.common.models

import rom.common.Context

data class ParamCorSettings(
    var paramIndex: Int = 0,
    val parent: Context = Context(),
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        other as ParamCorSettings

        if (paramIndex != other.paramIndex) return false
        if (parent != other.parent) return false
        return true
    }

    override fun hashCode(): Int {
        return this::class.hashCode()
    }

    companion object {
        val NONE = ParamCorSettings()
    }
}
