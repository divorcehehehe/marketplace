package rom.common.repo

import rom.common.helpers.errorSystem
import rom.common.models.*
import rom.common.repo.exceptions.RepoConcurrencyException
import rom.common.repo.exceptions.RepoException

const val ERROR_GROUP_REPO = "repo"

fun errorRepoConcurrency(
    oldModel: Model,
    expectedLock: ModelLock,
    exception: Exception = RepoConcurrencyException(
        id = oldModel.id,
        expectedLock = expectedLock,
        actualLock = oldModel.lock,
    ),
) = DbModelResponseErrWithData(
    model = oldModel,
    err = ROMError(
        code = "$ERROR_GROUP_REPO-concurrency",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "The object with ID ${oldModel.id.asString()} has been changed concurrently by another user or process",
        exception = exception,
    )
)

fun errorNotFound(id: ModelId) = DbModelResponseErr(
    ROMError(
        code = "$ERROR_GROUP_REPO-not-found",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Object with ID: ${id.asString()} is not Found",
    )
)

val errorEmptyId = DbModelResponseErr(
    ROMError(
        code = "$ERROR_GROUP_REPO-empty-id",
        group = ERROR_GROUP_REPO,
        field = "id",
        message = "Id must not be null or blank"
    )
)

val errorBadParamValues = DbModelResponseErr(
    ROMError(
        code = "$ERROR_GROUP_REPO-bad-paramValues",
        group = ERROR_GROUP_REPO,
        field = "paramValues",
        message = "ParamValues has wrong size"
    )
)

fun errorEmptyLock(id: ModelId) = DbModelResponseErr(
    ROMError(
        code = "$ERROR_GROUP_REPO-lock-empty",
        group = ERROR_GROUP_REPO,
        field = "lock",
        message = "Lock for Model ${id.asString()} is empty that is not admitted"
    )
)

fun errorRequestUserId(id: ModelId) = DbModelResponseErr(
    ROMError(
        code = "$ERROR_GROUP_REPO-requestUserId-empty",
        group = ERROR_GROUP_REPO,
        field = "requestUserId",
        message = "RequestUserId for Model ${id.asString()} is empty that is not admitted"
    )
)

fun errorRequestUserId() = DbModelsResponseErr(
    ROMError(
        code = "$ERROR_GROUP_REPO-requestUserId-empty",
        group = ERROR_GROUP_REPO,
        field = "requestUserId",
        message = "RequestUserId is empty that is not admitted"
    )
)

fun errorVisibility(id: ModelId, action: String) = DbModelResponseErr(
    ROMError(
        code = "$ERROR_GROUP_REPO-can-not-$action",
        group = ERROR_GROUP_REPO,
        field = "visibility",
        message = "Can not $action Model ${id.asString()}"
    )
)

fun errorDb(e: RepoException) = DbModelResponseErr(
    errorSystem(
        violationCode = "dbLockEmpty",
        e = e
    )
)
