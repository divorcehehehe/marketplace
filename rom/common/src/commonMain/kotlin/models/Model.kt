package rom.common.models

data class Model(
    var id: ModelId = ModelId.NONE,
    var ownerId: UserId = UserId.NONE,
    var lock: ModelLock = ModelLock.NONE,
    val permissionsClient: MutableSet<ModelPermissionClient> = mutableSetOf(),
    var field: Array<Double> = emptyArray(),
    var name: String = "",
    var macroPath: String = "",
    var solverPath: String = "",
    val params: MutableList<Param> = mutableListOf(),
    var sampling: Sampling = Sampling.NONE,
    var visibility: Visibility = Visibility.NONE,
    var paramValues: Array<Double> = emptyArray(),
) {
    fun isEmpty() = this == NONE
    companion object {
        private val NONE = Model()
    }
}
