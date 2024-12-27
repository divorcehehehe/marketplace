package rom.app.spring.repo

import com.ninjasquad.springmockk.MockkBean
import io.mockk.coEvery
import io.mockk.slot
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.context.annotation.Import
import org.springframework.test.web.reactive.server.WebTestClient
import rom.app.spring.config.ModelConfig
import rom.app.spring.controllers.ModelControllerV1Fine
import rom.common.repo.DbModelFilterRequest
import rom.common.repo.DbModelIdRequest
import rom.common.repo.DbModelRequest
import rom.common.repo.IRepoModel
import rom.repo.common.ModelRepoInitialized
import rom.repo.inmemory.ModelRepoInMemory
import rom.stubs.ModelStub
import kotlin.test.Test

// Temporary simple test with stubs
@WebFluxTest(
    ModelControllerV1Fine::class, ModelConfig::class,
    properties = ["spring.main.allow-bean-definition-overriding=true"]
)
@Import(RepoInMemoryConfig::class)
internal class ModelRepoInMemoryV1Test : ModelRepoBaseV1Test() {

    @Autowired
    override lateinit var webClient: WebTestClient

    @MockkBean
    @Qualifier("testRepo")
    lateinit var testRepo: IRepoModel

    @BeforeEach
    fun tearUp() {
        val slotModel = slot<DbModelRequest>()
        val slotId = slot<DbModelIdRequest>()
        val slotFl = slot<DbModelFilterRequest>()
        val repo = ModelRepoInitialized(
            repo = ModelRepoInMemory(randomUuid = { uuidNew }),
            initObjects = ModelStub.prepareSearchList("xx") + ModelStub.get()
        )
        coEvery { testRepo.createModel(capture(slotModel)) } coAnswers { repo.createModel(slotModel.captured) }
        coEvery { testRepo.readModel(capture(slotId)) } coAnswers { repo.readModel(slotId.captured) }
        coEvery { testRepo.updateModel(capture(slotModel)) } coAnswers { repo.updateModel(slotModel.captured) }
        coEvery { testRepo.deleteModel(capture(slotId)) } coAnswers { repo.deleteModel(slotId.captured) }
        coEvery { testRepo.searchModel(capture(slotFl)) } coAnswers { repo.searchModel(slotFl.captured) }
    }

    @Test
    override fun createModel() = super.createModel()

    @Test
    override fun readModel() = super.readModel()

    @Test
    override fun updateModel() = super.updateModel()

    @Test
    override fun deleteModel() = super.deleteModel()

    @Test
    override fun searchModel() = super.searchModel()

    @Test
    override fun trainModel() = super.trainModel()

    @Test
    override fun predictModel() = super.predictModel()
}
