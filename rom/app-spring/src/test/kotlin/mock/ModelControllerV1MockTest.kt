package rom.app.spring.mock

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.mockito.kotlin.any
import org.mockito.kotlin.wheneverBlocking
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import rom.app.spring.config.ModelConfig
import rom.app.spring.controllers.ModelControllerV1Fine
import rom.api.v1.models.*
import rom.biz.ModelProcessor
import rom.common.Context
import rom.mappers.v1.*
import rom.stubs.ModelStub
import kotlin.test.Test

// Temporary simple test with stubs
@WebFluxTest(ModelControllerV1Fine::class, ModelConfig::class)
internal class ModelControllerV1MockTest {
    @Autowired
    private lateinit var webClient: WebTestClient

    @Suppress("unused")
    @MockBean
    private lateinit var processor: ModelProcessor

    @BeforeEach
    fun tearUp() {
        wheneverBlocking { processor.exec(any()) }.then {
            it.getArgument<Context>(0).apply {
                modelResponse = ModelStub.get()
                modelsResponse = ModelStub.prepareSearchList("sdf").toMutableList()
            }
        }
    }

    @Test
    fun createModel() = testStubModel(
        "/v1/model/create",
        ModelCreateRequest(),
        Context(modelResponse = ModelStub.get()).toTransportCreate().copy(responseType = "create")
    )

    @Test
    fun readModel() = testStubModel(
        "/v1/model/read",
        ModelReadRequest(),
        Context(modelResponse = ModelStub.get()).toTransportRead().copy(responseType = "read")
    )

    @Test
    fun updateModel() = testStubModel(
        "/v1/model/update",
        ModelUpdateRequest(),
        Context(modelResponse = ModelStub.get()).toTransportUpdate().copy(responseType = "update")
    )

    @Test
    fun deleteModel() = testStubModel(
        "/v1/model/delete",
        ModelDeleteRequest(),
        Context(modelResponse = ModelStub.get()).toTransportDelete().copy(responseType = "delete")
    )

    @Test
    fun searchModel() = testStubModel(
        "/v1/model/search",
        ModelSearchRequest(),
        Context(modelsResponse = ModelStub.prepareSearchList("sdf").toMutableList())
            .toTransportSearch().copy(responseType = "search")
    )

    private inline fun <reified Req : Any, reified Res : Any> testStubModel(
        url: String,
        requestObj: Req,
        responseObj: Res,
    ) {
        webClient
            .post()
            .uri(url)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestObj))
            .exchange()
            .expectStatus().isOk
            .expectBody(Res::class.java)
            .value {
                println("RESPONSE: $it")
                assertThat(it).isEqualTo(responseObj)
            }
    }
}
