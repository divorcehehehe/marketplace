package rom.mappers.v1.exceptions

import rom.api.v1.models.BaseParam

class InvalidParamPosition(param: BaseParam) : Throwable(
    "Parameter position must be grater than zero at ${param.name}. (Given ${param.position})"
)
