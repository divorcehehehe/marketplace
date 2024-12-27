package rom.app.spring.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import rom.biz.ModelProcessor
import rom.common.CorSettings
import marketplace.logging.common.LoggerProvider
import marketplace.logging.jvm.loggerLogback
import rom.backend.repository.inmemory.ModelRepoStub
import rom.common.repo.IRepoModel
import rom.repo.inmemory.ModelRepoInMemory


@Suppress("unused")
@Configuration
class ModelConfig {
    @Bean
    fun processor(corSettings: CorSettings) = ModelProcessor(corSettings = corSettings)

    @Bean
    fun loggerProvider(): LoggerProvider = LoggerProvider { loggerLogback(it) }

    @Bean
    fun testRepo(): IRepoModel = ModelRepoInMemory()

    @Bean
    fun prodRepo(): IRepoModel = ModelRepoInMemory()

    @Bean
    fun stubRepo(): IRepoModel = ModelRepoStub()

    @Bean
    fun corSettings(): CorSettings = CorSettings(
        loggerProvider = loggerProvider(),
        repoTest = testRepo(),
        repoProd = prodRepo(),
        repoStub = stubRepo(),
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
