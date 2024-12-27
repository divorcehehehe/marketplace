package rom.api.log.mapper

import kotlinx.datetime.Clock
import rom.api.log.models.*
import rom.common.Context
import rom.common.models.*

fun Context.toLog(logId: String) = CommonLogSchema(
    messageTime = Clock.System.now().toString(),
    logId = logId,
    source = "education",
    model = toLog(),
    errors = errors.map { it.toLog() },
)

private fun Context.toLog(): ModelLogSchema? {
    val modelNone = Model()
    return ModelLogSchema(
        requestId = requestId.takeIf { it != RequestId.NONE }?.asString(),
        requestModel = modelRequest.takeIf { it != modelNone }?.toLog(),
        responseModel = modelResponse.takeIf { it != modelNone }?.toLog(),
        responseModels = modelsResponse.takeIf { it.isNotEmpty() }?.filter { it != modelNone }?.map { it.toLog() },
        requestFilter = modelFilterRequest.takeIf { it != ModelFilter() }?.toLog(),
    ).takeIf { it != ModelLogSchema() }
}

private fun ModelFilter.toLog() = ModelFilterLog(
    searchString = searchString.takeIf { it.isNotBlank() },
    requestUserId = requestUserId.takeIf { it != UserId.NONE }?.asString(),
)

private fun ROMError.toLog() = ErrorLogSchema(
    message = message.takeIf { it.isNotBlank() },
    field = field.takeIf { it.isNotBlank() },
    code = code.takeIf { it.isNotBlank() },
    level = level.name,
)

private fun Model.toLog(): ModelLog = ModelLog(
    id = id.takeIf { it != ModelId.NONE }?.asString(),
    ownerId = ownerId.takeIf { it != UserId.NONE }?.asString(),
    requestUserId = requestUserId.takeIf { it != UserId.NONE }?.asString(),
    field = field.takeIf { it.isNotEmpty() }?.toList(),
    name = name.takeIf { it.isNotBlank() },
    macroPath = macroPath.takeIf { it.isNotBlank() },
    solverPath = solverPath.takeIf { it.isNotBlank() },
    params = params.takeIf { it.isNotEmpty() }?.map { it.toParamLog() }?.toList(),
    sampling = sampling.takeIf { it != Sampling.NONE }?.name,
    visibility = visibility.takeIf { it != Visibility.NONE }?.name,
    paramValues = paramValues.takeIf { it.isNotEmpty() }?.toList(),
)

private fun Param.toParamLog(): ParamLog = ParamLog(
    id = paramId.takeIf { it != ParamId.NONE }?.asString(),
    modelId = modelId.takeIf { it != ModelId.NONE }?.asString(),
    line = line.takeIf { it != 0 },
    position = position.takeIf { it != 0 },
    separator = separator.takeIf { it.isNotBlank() },
    name = name.takeIf { it.isNotBlank() },
    units = units.takeIf { it.isNotBlank() },
    bounds = bounds.takeIf { it.isNotEmpty() }?.toList(),
)
