package rom.common.helpers

import rom.common.models.ROMError

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
