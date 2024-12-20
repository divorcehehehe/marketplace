package rom.app.common

import rom.biz.ModelProcessor
import rom.common.CorSettings

interface IAppSettings {
    val processor: ModelProcessor
    val corSettings: CorSettings
}
