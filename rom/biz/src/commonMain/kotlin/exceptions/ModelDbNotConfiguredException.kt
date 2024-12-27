package rom.biz.exceptions

import rom.common.models.WorkMode

class ModelDbNotConfiguredException(val workMode: WorkMode): Exception(
    "Database is not configured properly for workmode $workMode"
)
