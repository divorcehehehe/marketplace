package rom.common.repo

import rom.common.models.UserId

data class DbModelFilterRequest(
    val nameFilter: String = "",
    val requestUserId: UserId,
)
