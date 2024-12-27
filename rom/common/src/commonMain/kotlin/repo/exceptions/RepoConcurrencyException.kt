package rom.common.repo.exceptions

import rom.common.models.ModelId
import rom.common.models.ModelLock

class RepoConcurrencyException(id: ModelId, expectedLock: ModelLock, actualLock: ModelLock?): RepoModelException(
    id, "Expected lock is $expectedLock while actual lock in db is $actualLock"
)
