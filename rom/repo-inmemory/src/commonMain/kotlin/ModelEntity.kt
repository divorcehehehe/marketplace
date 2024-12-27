package rom.repo.inmemory

import rom.common.models.*

data class ModelEntity(
    val id: String? = null,
    val ownerId: String? = null,
    val lock: String? = null,
    val field: Array<Double>? = null,
    val name: String? = null,
    val macroPath: String? = null,
    val solverPath: String? = null,
    val params: List<ParamEntity>? = null,
    val sampling: String? = null,
    val visibility: String? = null,
    val paramValues: Array<Double>? = null,
    val usVector: Array<Double>? = null,
    val vtVector: Array<Double>? = null,
) {
    constructor(model: Model): this(
        id = model.id.asString().takeIf { it.isNotBlank() },
        ownerId = model.ownerId.asString().takeIf { it.isNotBlank() },
        lock = model.lock.asString().takeIf { it.isNotBlank() },
        field = model.field.takeIf { it.isNotEmpty() },
        name = model.name.takeIf { it.isNotBlank() },
        macroPath = model.macroPath.takeIf { it.isNotBlank() },
        solverPath = model.solverPath.takeIf { it.isNotBlank() },
        params = model.params.takeIf { it.isNotEmpty() }?.map { ParamEntity(it) },
        sampling = model.sampling.takeIf { it != Sampling.NONE }?.name,
        visibility = model.visibility.takeIf { it != Visibility.NONE }?.name,
        paramValues = model.paramValues.takeIf { it.isNotEmpty() },
        usVector = model.usVector.takeIf { it.isNotEmpty() },
        vtVector = model.vtVector.takeIf { it.isNotEmpty() },
    )

    fun toInternal() = Model(
        id = id?.let { ModelId(it) } ?: ModelId.NONE,
        ownerId = ownerId?.let { UserId(it) } ?: UserId.NONE,
        lock = lock?.let { ModelLock(it) } ?: ModelLock.NONE,
        field = field ?: arrayOf(),
        name = name ?: "",
        macroPath = macroPath ?: "",
        solverPath = solverPath ?: "",
        params = params?.map { it.toInternal() }?.toMutableList() ?: mutableListOf(),
        sampling = sampling?.let { Sampling.valueOf(it) } ?: Sampling.NONE,
        visibility = visibility?.let { Visibility.valueOf(it) } ?: Visibility.NONE,
        paramValues = paramValues ?: arrayOf(),
        usVector = usVector ?: arrayOf(),
        vtVector = vtVector ?: arrayOf(),
    )
}
