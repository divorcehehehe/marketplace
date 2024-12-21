package rom.app.rabbit.config

import rom.app.common.IAppSettings

interface IAppRabbitSettings: IAppSettings {
    val rabbit: RabbitConfig
    val controllersConfigV1: RabbitExchangeConfiguration
    val controllersConfigV2: RabbitExchangeConfiguration
}
