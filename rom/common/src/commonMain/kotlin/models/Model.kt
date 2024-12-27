package rom.common.models

data class Model(
    var id: ModelId = ModelId.NONE,
    var ownerId: UserId = UserId.NONE,
    var requestUserId: UserId = UserId.NONE,
    var lock: ModelLock = ModelLock.NONE,
    var field: Array<Double> = emptyArray(),
    var name: String = "",
    var macroPath: String = "",
    var solverPath: String = "",
    var params: MutableList<Param> = mutableListOf(),
    var sampling: Sampling = Sampling.NONE,
    var visibility: Visibility = Visibility.NONE,
    var paramValues: Array<Double> = emptyArray(),

//    ROM related vectors
    var usVector: Array<Double> = emptyArray(),
    var vtVector: Array<Double> = emptyArray(),
) {
    fun isEmpty() = this == NONE

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Model

        if (id != other.id) return false
        if (ownerId != other.ownerId) return false
        if (lock != other.lock) return false
        if (!field.contentEquals(other.field)) return false
        if (name != other.name) return false
        if (macroPath != other.macroPath) return false
        if (solverPath != other.solverPath) return false
        if (params != other.params) return false
        if (sampling != other.sampling) return false
        if (visibility != other.visibility) return false
        if (!paramValues.contentEquals(other.paramValues)) return false

        return true
    }

    override fun hashCode(): Int {
        return this::class.hashCode()
    }

    companion object {
        private val NONE = Model()
    }
}
