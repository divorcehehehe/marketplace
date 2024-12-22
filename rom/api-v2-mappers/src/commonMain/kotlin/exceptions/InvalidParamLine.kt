package rom.mappers.v2.exceptions

import rom.api.v2.models.BaseParam

class InvalidParamLine(param: BaseParam) : Throwable(
    "Parameter line must be grater than zero at ${param.name}. (Given ${param.line})"
)
