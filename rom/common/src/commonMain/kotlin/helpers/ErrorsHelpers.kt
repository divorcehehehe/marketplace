package rom.common.helpers

import marketplace.logging.common.LogLevel
import rom.common.Context
import rom.common.models.ROMError
import rom.common.models.State

fun Throwable.asError(
    code: String = "unknown",
    group: String = "exceptions",
    message: String = this.message ?: "",
) = ROMError(
    code = code,
    group = group,
    field = "",
    message = message,
    exception = this,
)

fun Context.addError(error: ROMError) = errors.add(error)
fun Context.addErrors(error_collection: Collection<ROMError>) = errors.addAll(error_collection)

fun Context.fail(error: ROMError) {
    addError(error)
    state = State.FAILING
}

fun Context.fail(errors: Collection<ROMError>) {
    addErrors(errors)
    state = State.FAILING
}

fun errorValidation(
    field: String,
    /**
     * Код, характеризующий ошибку. Не должен включать имя поля или указание на валидацию.
     * Например: empty, badSymbols, tooLong, etc
     */
    violationCode: String,
    description: String,
    level: LogLevel = LogLevel.ERROR,
) = ROMError(
    code = "validation-$field-$violationCode",
    field = field,
    group = "validation",
    message = "Validation error for field $field: $description",
    level = level,
)

fun errorSystem(
    violationCode: String,
    level: LogLevel = LogLevel.ERROR,
    e: Throwable,
) = ROMError(
    code = "system-$violationCode",
    group = "system",
    message = "System error occurred. Our stuff has been informed, please retry later",
    level = level,
    exception = e,
)
