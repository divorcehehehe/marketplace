package rom.mappers.v1.exceptions

import rom.api.v1.models.BaseParam

class InvalidParamLine(param: BaseParam) : Throwable(
    "Parameter line must be grater than zero at ${param.name}. (Given ${param.line})"
)
