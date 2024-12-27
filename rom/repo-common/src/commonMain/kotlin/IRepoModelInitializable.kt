package rom.repo.common

import rom.common.models.Model
import rom.common.repo.IRepoModel

interface IRepoModelInitializable: IRepoModel {
    fun save(models: Collection<Model>): Collection<Model>
}
