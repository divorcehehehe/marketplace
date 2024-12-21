package rom.app.rabbit.config

import rom.common.CorSettings
import rom.app.common.IAppSettings
import rom.biz.ModelProcessor

data class AppSettings(
    override val corSettings: CorSettings = CorSettings(),
    override val processor: ModelProcessor = ModelProcessor(corSettings),
    override val rabbit: RabbitConfig = RabbitConfig(),
    override val controllersConfigV1: RabbitExchangeConfiguration = RabbitExchangeConfiguration.NONE,
    override val controllersConfigV2: RabbitExchangeConfiguration = RabbitExchangeConfiguration.NONE,
): IAppSettings, IAppRabbitSettings
