package rom.common.models

data class ModelFilter(
    var searchString: String = "",
    var requestUserId: UserId = UserId.NONE,
) {
    fun isEmpty() = this == NONE
    companion object {
        private val NONE = ModelFilter()
    }
}
