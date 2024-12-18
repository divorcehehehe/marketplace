package rom.mappers.v1

import rom.api.v1.models.*
import rom.common.Context
import rom.common.exceptions.UnknownCommand
import rom.common.models.*

fun Context.toTransportModel(): IResponse = when (val cmd = command) {
    Command.CREATE -> toTransportCreate()
    Command.READ -> toTransportRead()
    Command.UPDATE -> toTransportUpdate()
    Command.DELETE -> toTransportDelete()
    Command.SEARCH -> toTransportSearch()
    Command.TRAIN -> toTransportTrain()
    Command.PREDICT -> toTransportPredict()
    Command.NONE -> throw UnknownCommand(cmd)
}

fun Context.toTransportCreate() = ModelCreateResponse(
    responseType = "create",
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    model = modelResponse.toTransportModel()
)

fun Context.toTransportRead() = ModelReadResponse(
    responseType = "read",
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    model = modelResponse.toTransportModel()
)

fun Context.toTransportUpdate() = ModelUpdateResponse(
    responseType = "update",
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    model = modelResponse.toTransportModel()
)

fun Context.toTransportDelete() = ModelDeleteResponse(
    responseType = "delete",
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    model = modelResponse.toTransportModel()
)

fun Context.toTransportSearch() = ModelSearchResponse(
    responseType = "search",
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    models = modelsResponse.toTransportModel()
)

fun Context.toTransportTrain() = ModelTrainResponse(
    responseType = "train",
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    model = modelResponse.toTransportModel()
)

fun Context.toTransportPredict() = ModelPredictResponse(
    responseType = "predict",
    result = state.toResult(),
    errors = errors.toTransportErrors(),
    model = modelResponse.toTransportModel()
)

fun List<Model>.toTransportModel(): List<ModelResponseObject>? = this
    .map { it.toTransportModel() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun Model.toTransportModel(): ModelResponseObject = ModelResponseObject(
    id = id.takeIf { it != ModelId.NONE }?.asString(),
    ownerId = ownerId.takeIf { it != UserId.NONE }?.asString(),
    lock = lock.takeIf { it != ModelLock.NONE }?.asString(),
    permissions = permissionsClient.toTransportModel(),
    field = field.takeIf { it.isNotEmpty() }?.toList(),
    name = name.takeIf { it.isNotBlank() },
    macroPath = macroPath.takeIf { it.isNotBlank() },
    solverPath = solverPath.takeIf { it.isNotBlank() },
    params = params.toTransportParam(),
    sampling = sampling.toTransportModel(),
    visibility = visibility.toTransportModel(),
    paramValues = paramValues.takeIf { it.isNotEmpty() }?.toList(),
)

private fun MutableList<Param>.toTransportParam(): List<ParamResponseObject>? = this
    .map { it.toTransportParam() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun Param.toTransportParam(): ParamResponseObject = ParamResponseObject(
    line = line.takeIf { it != 0 },
    position = position.takeIf { it != 0 },
    separator = separator.takeIf { it.isNotBlank() },
    name = name.takeIf { it.isNotBlank() },
    units = units.takeIf { it.isNotBlank() },
    bounds = bounds.takeIf { it.isNotEmpty() }?.toList(),
    id = paramId.takeIf { it != ParamId.NONE }?.asString(),
    modelId = modelId.takeIf { it != ModelId.NONE }?.asString()
)

private fun Sampling.toTransportModel() = when (this) {
    Sampling.LATIN_HYPER_CUBE -> ModelSampling.LATIN_HYPER_CUBE
    Sampling.ADAPTIVE_SAMPLING -> ModelSampling.ADAPTIVE_SAMPLING
    Sampling.NONE -> null
}

private fun Set<ModelPermissionClient>.toTransportModel(): Set<ModelPermissions>? = this
    .map { it.toTransportModel() }
    .toSet()
    .takeIf { it.isNotEmpty() }

private fun ModelPermissionClient.toTransportModel() = when (this) {
    ModelPermissionClient.READ -> ModelPermissions.READ
    ModelPermissionClient.UPDATE -> ModelPermissions.UPDATE
    ModelPermissionClient.DELETE -> ModelPermissions.DELETE
    ModelPermissionClient.MAKE_VISIBLE_PUBLIC -> ModelPermissions.MAKE_VISIBLE_PUBLIC
    ModelPermissionClient.MAKE_VISIBLE_OWNER -> ModelPermissions.MAKE_VISIBLE_OWN
    ModelPermissionClient.MAKE_VISIBLE_GROUP -> ModelPermissions.MAKE_VISIBLE_GROUP
}

private fun Visibility.toTransportModel(): ModelVisibility? = when (this) {
    Visibility.VISIBLE_PUBLIC -> ModelVisibility.PUBLIC
    Visibility.VISIBLE_TO_GROUP -> ModelVisibility.REGISTERED_ONLY
    Visibility.VISIBLE_TO_OWNER -> ModelVisibility.OWNER_ONLY
    Visibility.NONE -> null
}

private fun List<ROMError>.toTransportErrors(): List<Error>? = this
    .map { it.toTransportModel() }
    .toList()
    .takeIf { it.isNotEmpty() }

private fun ROMError.toTransportModel() = Error(
    code = code.takeIf { it.isNotBlank() },
    group = group.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    message = message.takeIf { it.isNotBlank() },
)

private fun State.toResult(): ResponseResult? = when (this) {
    State.RUNNING -> ResponseResult.SUCCESS
    State.FAILING -> ResponseResult.ERROR
    State.FINISHING -> ResponseResult.SUCCESS
    State.NONE -> null
}
