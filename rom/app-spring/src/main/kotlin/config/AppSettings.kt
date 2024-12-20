package rom.app.spring.config

import rom.app.common.IAppSettings
import rom.biz.ModelProcessor
import rom.common.CorSettings

data class AppSettings(
    override val corSettings: CorSettings,
    override val processor: ModelProcessor,
): IAppSettings
