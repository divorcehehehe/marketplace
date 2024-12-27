package rom.common.repo.exceptions

import rom.common.models.ModelId

open class RepoModelException(
    @Suppress("unused")
    val modelId: ModelId,
    msg: String,
): RepoException(msg)
