package rom.repo.common

import rom.common.models.Model

/**
 * Делегат для всех репозиториев, позволяющий инициализировать базу данных предзагруженными данными
 */
class ModelRepoInitialized(
    val repo: IRepoModelInitializable,
    initObjects: Collection<Model> = emptyList(),
) : IRepoModelInitializable by repo {
    @Suppress("unused")
    val initializedObjects: List<Model> = save(initObjects).toList()
}
