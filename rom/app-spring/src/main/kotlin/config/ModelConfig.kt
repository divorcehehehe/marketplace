package rom.app.spring.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import rom.biz.ModelProcessor
import rom.common.CorSettings
import marketplace.logging.common.LoggerProvider
import marketplace.logging.jvm.loggerLogback

@Suppress("unused")
@Configuration
class ModelConfig {
    @Bean
    fun processor(corSettings: CorSettings) = ModelProcessor(corSettings = corSettings)

    @Bean
    fun loggerProvider(): LoggerProvider = LoggerProvider { loggerLogback(it) }

    @Bean
    fun corSettings(): CorSettings = CorSettings(
        loggerProvider = loggerProvider(),
    )

    @Bean
    fun appSettings(
        corSettings: CorSettings,
        processor: ModelProcessor,
    ) = AppSettings(
        corSettings = corSettings,
        processor = processor,
    )
}
