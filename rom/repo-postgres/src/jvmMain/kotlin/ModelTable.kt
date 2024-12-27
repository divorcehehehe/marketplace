package rom.backend.repo.postgresql

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.json.jsonb
import org.jetbrains.exposed.sql.statements.UpdateBuilder
import kotlinx.serialization.json.Json
import rom.common.models.*

class ModelTable(tableName: String) : Table(tableName) {
    val id = text(SqlFields.ID)
    val lock = text(SqlFields.LOCK)
    val ownerId = text(SqlFields.OWNER_ID)
    val name = text(SqlFields.NAME).nullable()
    val macroPath = text(SqlFields.MACRO_PATH).nullable()
    val solverPath = text(SqlFields.SOLVER_PATH).nullable()
    val sampling = samplingEnumeration(SqlFields.SAMPLING).nullable()
    val params = jsonb(
        "params",
        { value ->
            Json.encodeToString(
                JsonParams.serializer(),
                value
            )
        },
        { value ->
            Json.decodeFromString(
                JsonParams.serializer(),
                value
            )
        }
    ).nullable()
    val usVector = array<String>(SqlFields.US_VECTOR).nullable()
    val vtVector = array<String>(SqlFields.VT_VECTOR).nullable()
    val visibility = visibilityEnumeration(SqlFields.VISIBILITY).nullable()

    override val primaryKey = PrimaryKey(id)

    fun from(res: ResultRow) = Model(
        id = ModelId(res[id]),
        lock = ModelLock(res[lock]),
        ownerId = UserId(res[ownerId]),
        name = res[name] ?: "",
        macroPath = res[macroPath] ?: "",
        solverPath = res[solverPath] ?: "",
        sampling = res[sampling] ?: Sampling.NONE,
        params = res[params]?.params?.map { it.toParam() }?.toMutableList() ?: mutableListOf(),
        usVector = res[usVector]?.map { it.toDouble() }?.toTypedArray() ?: arrayOf(),
        vtVector = res[vtVector]?.map { it.toDouble() }?.toTypedArray() ?: arrayOf(),
        visibility = res[visibility] ?: Visibility.NONE,
    )

    private fun JsonParam.toParam() = Param(
        line = line,
        position = position,
        separator = separator,
        name = name,
        units = units,
        bounds = bounds.toMutableList(),
        paramId = ParamId(paramId),
        modelId = ModelId(modelId),
    )

    private fun Param.toJsonParam(
        randomUuid: () -> String,
        inputModelId: String,
    ) = JsonParam(
        line = line,
        position = position,
        separator = separator,
        name = name,
        units = units,
        bounds = bounds.toMutableList(),
        paramId = randomUuid(),
        modelId = inputModelId,
    )

    fun to(it: UpdateBuilder<*>, model: Model, randomUuid: () -> String) {
        val modelId = model.id.takeIf { it != ModelId.NONE }?.asString() ?: randomUuid()
        it[id] = modelId
        it[lock] = model.lock.takeIf { it != ModelLock.NONE }?.asString() ?: randomUuid()
        if (model.ownerId != UserId.NONE)
            it[ownerId] = model.ownerId.asString()
        it[name] = model.name
        it[solverPath] = model.solverPath
        it[macroPath] = model.macroPath
        it[sampling] = model.sampling
        it[params] = JsonParams(model.params.map { it.toJsonParam(randomUuid, modelId) })
        it[usVector] = model.usVector.map { it.toString() }
        it[vtVector] = model.vtVector.map { it.toString() }
        it[visibility] = model.visibility
    }

}
