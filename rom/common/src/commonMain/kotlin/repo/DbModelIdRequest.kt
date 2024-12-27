package rom.common.repo

import rom.common.models.Model
import rom.common.models.ModelId
import rom.common.models.ModelLock
import rom.common.models.UserId

data class DbModelIdRequest(
    val id: ModelId,
    val requestUserId: UserId = UserId.NONE,
    val lock: ModelLock = ModelLock.NONE,
) {
    constructor(model: Model): this(
        model.id,
        model.requestUserId,
        model.lock,
    )
}
