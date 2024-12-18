package rom.mappers.v1

import rom.api.v1.models.*
import rom.common.Context
import rom.common.models.*
import rom.common.models.Param
import rom.common.models.WorkMode
import rom.common.stubs.Stubs
import rom.mappers.v1.exceptions.InvalidParamLine
import rom.mappers.v1.exceptions.UnknownRequestClass
import rom.mappers.v1.exceptions.InvalidParamPosition

fun Context.fromTransport(request: IRequest) = when (request) {
    is ModelCreateRequest -> fromTransport(request)
    is ModelReadRequest -> fromTransport(request)
    is ModelUpdateRequest -> fromTransport(request)
    is ModelDeleteRequest -> fromTransport(request)
    is ModelSearchRequest -> fromTransport(request)
    is ModelTrainRequest -> fromTransport(request)
    is ModelPredictRequest -> fromTransport(request)
    else -> throw UnknownRequestClass(request.javaClass)
}

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
    ModelRequestDebugStubs.BAD_TITLE -> Stubs.BAD_TITLE
    ModelRequestDebugStubs.BAD_DESCRIPTION -> Stubs.BAD_DESCRIPTION
    ModelRequestDebugStubs.BAD_VISIBILITY -> Stubs.BAD_VISIBILITY
    ModelRequestDebugStubs.CANNOT_DELETE -> Stubs.CANNOT_DELETE
    ModelRequestDebugStubs.BAD_SEARCH_STRING -> Stubs.BAD_SEARCH_STRING
    null -> Stubs.NONE
}

fun Context.fromTransport(request: ModelCreateRequest) {
    command = Command.CREATE
    modelRequest = request.model?.toInternal() ?: Model()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun Context.fromTransport(request: ModelReadRequest) {
    command = Command.READ
    modelRequest = request.model?.toInternal() ?: Model()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun Context.fromTransport(request: ModelUpdateRequest) {
    command = Command.UPDATE
    modelRequest = request.model?.toInternal() ?: Model()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun Context.fromTransport(request: ModelDeleteRequest) {
    command = Command.DELETE
    modelRequest = request.model?.toInternal() ?: Model()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun Context.fromTransport(request: ModelSearchRequest) {
    command = Command.SEARCH
    modelFilterRequest = request.modelFilter?.toInternal() ?: ModelFilter()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun Context.fromTransport(request: ModelTrainRequest) {
    command = Command.TRAIN
    modelRequest = request.model?.toInternal() ?: Model()
    workMode = request.debug.transportToWorkMode()
    stubCase = request.debug.transportToStubCase()
}

fun Context.fromTransport(request: ModelPredictRequest) {
    command = Command.PREDICT
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
)

private fun ModelPredictObject.toInternal(): Model = Model(
    id = id.toModelId(),
    paramValues = paramValues?.toTypedArray() ?: emptyArray(),
)

private fun ModelSearchFilter.toInternal(): ModelFilter = ModelFilter(
    searchString = this.searchString ?: ""
)

private fun ModelCreateObject.toInternal(): Model = Model(
    name = this.name ?: "",
    macroPath = this.macroPath ?: "",
    solverPath = this.solverPath ?: "",
    params = this.params?.map { it.validate().toInternal() }?.toMutableList() ?: mutableListOf(),
    sampling = this.sampling.fromTransport(),
    visibility = this.visibility.fromTransport(),
)

private fun BaseParam.validate(): BaseParam {
    this.position?.let { if (it < 1) throw InvalidParamPosition(this) }
    this.line?.let { if (it < 1) throw InvalidParamLine(this) }

    return this
}

private fun ModelUpdateObject.toInternal(): Model = Model(
    name = this.name ?: "",
    macroPath = this.macroPath ?: "",
    solverPath = this.solverPath ?: "",
    params = this.params?.map { it.validate().toInternal() }?.toMutableList() ?: mutableListOf(),
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
