package rom.common.models

data class ModelFilter(
    var searchString: String = "",
    var ownerId: UserId = UserId.NONE
) {
    fun deepCopy(): ModelFilter = copy()
    fun isEmpty() = this == NONE
    companion object {
        private val NONE = ModelFilter()
    }
}
