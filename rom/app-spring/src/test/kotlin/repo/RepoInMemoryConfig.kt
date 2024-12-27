package rom.app.spring.repo

import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import rom.common.repo.IRepoModel
import rom.repo.inmemory.ModelRepoInMemory

@TestConfiguration
class RepoInMemoryConfig {
    @Suppress("unused")
    @Bean()
    @Primary
    fun prodRepo(): IRepoModel = ModelRepoInMemory()
}
