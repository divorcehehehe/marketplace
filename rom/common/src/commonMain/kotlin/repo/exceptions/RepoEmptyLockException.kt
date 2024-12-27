package rom.common.repo.exceptions

import rom.common.models.ModelId

class RepoEmptyLockException(id: ModelId): RepoModelException(
    id, "Lock is empty in DB"
)
