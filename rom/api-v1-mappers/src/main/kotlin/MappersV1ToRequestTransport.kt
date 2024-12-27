package rom.mappers.v1

import rom.api.v1.models.*
import rom.common.models.*

fun Model.toTransportCreate() = ModelCreateObject(
    name = name.takeIf { it.isNotBlank() },
    macroPath = macroPath.takeIf { it.isNotBlank() },
    solverPath = solverPath.takeIf { it.isNotBlank() },
    params = params.takeIf { it.isNotEmpty() }?.map { it.toBaseParam() },
    sampling = sampling.toTransportModel(),
    visibility = visibility.toTransportModel(),
    requestUserId = requestUserId.takeIf { it != UserId.NONE }?.asString(),
)

fun Model.toTransportRead() = ModelReadObject(
    id = id.takeIf { it != ModelId.NONE }?.asString(),
    requestUserId = requestUserId.takeIf { it != UserId.NONE }?.asString(),
)

fun Model.toTransportUpdate() = ModelUpdateObject(
    name = name.takeIf { it.isNotBlank() },
    macroPath = macroPath.takeIf { it.isNotBlank() },
    solverPath = solverPath.takeIf { it.isNotBlank() },
    params = params.takeIf { it.isNotEmpty() }?.map { it.toBaseParam() },
    sampling = sampling.toTransportModel(),
    visibility = visibility.toTransportModel(),
    id = id.takeIf { it != ModelId.NONE }?.asString(),
    lock = lock.takeIf { it != ModelLock.NONE }?.asString(),
    requestUserId = requestUserId.takeIf { it != UserId.NONE }?.asString(),
)

fun Model.toTransportDelete() = ModelDeleteObject(
    id = id.takeIf { it != ModelId.NONE }?.asString(),
    lock = lock.takeIf { it != ModelLock.NONE }?.asString(),
    requestUserId = requestUserId.takeIf { it != UserId.NONE }?.asString(),
)

fun Model.toTransportTrain() = ModelTrainObject(
    id = id.takeIf { it != ModelId.NONE }?.asString(),
    lock = lock.takeIf { it != ModelLock.NONE }?.asString(),
    requestUserId = requestUserId.takeIf { it != UserId.NONE }?.asString(),
)

fun Model.toTransportPredict() = ModelPredictObject(
    id = id.takeIf { it != ModelId.NONE }?.asString(),
    lock = lock.takeIf { it != ModelLock.NONE }?.asString(),
    requestUserId = requestUserId.takeIf { it != UserId.NONE }?.asString(),
    paramValues = paramValues.takeIf { it.isNotEmpty() }?.toList()
)

fun Param.toBaseParam() = BaseParam(
    line = line.takeIf { it != 0 },
    position = position.takeIf { it != 0 },
    separator = separator.takeIf { it.isNotBlank() },
    name = name.takeIf { it.isNotBlank() },
    units = units.takeIf { it.isNotBlank() },
    bounds = bounds.takeIf { it.isNotEmpty() }
)
