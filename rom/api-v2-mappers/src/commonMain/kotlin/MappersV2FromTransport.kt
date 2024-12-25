package rom.mappers.v2

import rom.api.v2.models.*
import rom.common.Context
import rom.common.models.*
import rom.common.models.WorkMode
import rom.common.stubs.Stubs

fun Context.fromTransport(request: IRequest) = when (request) {
    is ModelCreateRequest -> fromTransport(request)
    is ModelReadRequest -> fromTransport(request)
    is ModelUpdateRequest -> fromTransport(request)
    is ModelDeleteRequest -> fromTransport(request)
    is ModelSearchRequest -> fromTransport(request)
    is ModelTrainRequest -> fromTransport(request)
    is ModelPredictRequest -> fromTransport(request)
}

private fun String?.toUserId() = this?.let { UserId(it) } ?: UserId.NONE
private fun String?.toModelId() = this?.let { ModelId(it) } ?: ModelId.NONE
private fun String?.toModelLock() = this?.let { ModelLock(it) } ?: ModelLock.NONE

private fun ModelDebug?.transportToWorkMode(): WorkMode = when (this?.mode) {
    ModelRequestDebugMode.PROD -> WorkMode.PROD
    ModelRequestDebugMode.TEST -> WorkMode.TEST
    ModelRequestDebugMode.STUB -> WorkMode.STUB
    null -> WorkMode.PROD
}

private fun ModelDebug?.transportToStubCase(): Stubs = when (this?.stub) {
    ModelRequestDebugStubs.SUCCESS -> Stubs.SUCCESS
    ModelRequestDebugStubs.NOT_FOUND -> Stubs.NOT_FOUND
    ModelRequestDebugStubs.BAD_ID -> Stubs.BAD_ID
    ModelRequestDebugStubs.BAD_LOCK -> Stubs.BAD_LOCK
    ModelRequestDebugStubs.BAD_NAME -> Stubs.BAD_NAME
    ModelRequestDebugStubs.BAD_MACRO_PATH -> Stubs.BAD_MACRO_PATH
    ModelRequestDebugStubs.BAD_SOLVER_PATH -> Stubs.BAD_SOLVER_PATH
    ModelRequestDebugStubs.BAD_PARAM_LINE -> Stubs.BAD_PARAM_LINE
    ModelRequestDebugStubs.BAD_PARAM_POSITION -> Stubs.BAD_PARAM_POSITION
    ModelRequestDebugStubs.BAD_PARAM_SEPARATOR -> Stubs.BAD_PARAM_SEPARATOR
    ModelRequestDebugStubs.BAD_PARAM_NAME -> Stubs.BAD_PARAM_NAME
    ModelRequestDebugStubs.BAD_PARAM_UNITS -> Stubs.BAD_PARAM_UNITS
    ModelRequestDebugStubs.BAD_PARAM_BOUNDS -> Stubs.BAD_PARAM_BOUNDS
    ModelRequestDebugStubs.BAD_SAMPLING -> Stubs.BAD_SAMPLING
    ModelRequestDebugStubs.BAD_VISIBILITY -> Stubs.BAD_VISIBILITY
    ModelRequestDebugStubs.CANNOT_DELETE -> Stubs.CANNOT_DELETE
    ModelRequestDebugStubs.CANNOT_UPDATE -> Stubs.CANNOT_UPDATE
    ModelRequestDebugStubs.BAD_SEARCH_STRING -> Stubs.BAD_SEARCH_STRING
    ModelRequestDebugStubs.BAD_PARAM_VALUES -> Stubs.BAD_PARAM_VALUES
    ModelRequestDebugStubs.DB_ERROR -> Stubs.DB_ERROR
    null -> Stubs.NONE
}

fun Context.fromTransport(request: ModelCreateRequest) {
    command = Command.CREATE
    requestUserId = request.requestUserId.toUserId()
    modelRequest = request.model?.toInternal(requestUserId) ?: Model()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun Context.fromTransport(request: ModelReadRequest) {
    command = Command.READ
    requestUserId = request.requestUserId.toUserId()
    modelRequest = request.model?.toInternal() ?: Model()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun Context.fromTransport(request: ModelUpdateRequest) {
    command = Command.UPDATE
    requestUserId = request.requestUserId.toUserId()
    modelRequest = request.model?.toInternal() ?: Model()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun Context.fromTransport(request: ModelDeleteRequest) {
    command = Command.DELETE
    requestUserId = request.requestUserId.toUserId()
    modelRequest = request.model?.toInternal() ?: Model()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun Context.fromTransport(request: ModelSearchRequest) {
    command = Command.SEARCH
    requestUserId = request.requestUserId.toUserId()
    modelFilterRequest = request.modelFilter?.toInternal() ?: ModelFilter()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun Context.fromTransport(request: ModelTrainRequest) {
    command = Command.TRAIN
    requestUserId = request.requestUserId.toUserId()
    modelRequest = request.model?.toInternal() ?: Model()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun Context.fromTransport(request: ModelPredictRequest) {
    command = Command.PREDICT
    requestUserId = request.requestUserId.toUserId()
    modelRequest = request.model?.toInternal() ?: Model()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

private fun ModelReadObject.toInternal(): Model = Model(
    id = id.toModelId()
)

private fun ModelDeleteObject.toInternal(): Model = Model(
    id = id.toModelId(),
    lock = lock.toModelLock(),
)

private fun ModelTrainObject.toInternal(): Model = Model(
    id = id.toModelId(),
    lock = lock.toModelLock(),
)

private fun ModelPredictObject.toInternal(): Model = Model(
    id = id.toModelId(),
    lock = lock.toModelLock(),
    paramValues = paramValues?.toTypedArray() ?: emptyArray(),
)

private fun ModelSearchFilter.toInternal(): ModelFilter = ModelFilter(
    searchString = this.searchString ?: ""
)

private fun ModelCreateObject.toInternal(requestUserId: UserId): Model = Model(
    ownerId = requestUserId,
    name = this.name ?: "",
    macroPath = this.macroPath ?: "",
    solverPath = this.solverPath ?: "",
    params = this.params?.map { it.toInternal() }?.toMutableList() ?: mutableListOf(),
    sampling = this.sampling.fromTransport(),
    visibility = this.visibility.fromTransport(),
)

private fun ModelUpdateObject.toInternal(): Model = Model(
    name = this.name ?: "",
    macroPath = this.macroPath ?: "",
    solverPath = this.solverPath ?: "",
    params = this.params?.map { it.toInternal() }?.toMutableList() ?: mutableListOf(),
    sampling = this.sampling.fromTransport(),
    visibility = this.visibility.fromTransport(),
    id = this.id.toModelId(),
    lock = lock.toModelLock(),
)

private fun BaseParam.toInternal(): Param = Param(
    line = this.line ?: 0,
    position = this.position ?: 0,
    separator = this.separator ?: "",
    name = this.name ?: "",
    units = this.units ?: "",
    bounds = this.bounds?.toMutableList() ?: mutableListOf(),
)

private fun ModelSampling?.fromTransport(): Sampling = when (this) {
    ModelSampling.ADAPTIVE_SAMPLING -> Sampling.ADAPTIVE_SAMPLING
    ModelSampling.LATIN_HYPER_CUBE -> Sampling.LATIN_HYPER_CUBE
    null -> Sampling.NONE
}

private fun ModelVisibility?.fromTransport(): Visibility = when (this) {
    ModelVisibility.PUBLIC -> Visibility.VISIBLE_PUBLIC
    ModelVisibility.OWNER_ONLY -> Visibility.VISIBLE_TO_OWNER
    ModelVisibility.REGISTERED_ONLY -> Visibility.VISIBLE_TO_GROUP
    null -> Visibility.NONE
}
