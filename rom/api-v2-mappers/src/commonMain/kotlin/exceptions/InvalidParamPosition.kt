package rom.mappers.v2.exceptions

import rom.api.v2.models.BaseParam

class InvalidParamPosition(param: BaseParam) : Throwable(
    "Parameter position must be grater than zero at ${param.name}. (Given ${param.position})"
)
