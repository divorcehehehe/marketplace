package rom.app.spring.mock

import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import rom.app.spring.config.ModelConfig
import rom.app.spring.controllers.ModelControllerV1Fine
import rom.api.v1.models.*
import rom.common.Context
import rom.common.models.State
import rom.mappers.v1.*
import rom.stubs.ModelStub
import kotlin.test.Test

// Temporary simple test with stubs
@WebFluxTest(ModelControllerV1Fine::class, ModelConfig::class)
internal class ModelControllerV1StubTest {
    @Autowired
    private lateinit var webClient: WebTestClient

    @Test
    fun createModel() = testStubModel(
        "/v1/model/create",
        ModelCreateRequest(),
        Context(modelResponse = ModelStub.get(), state = State.FINISHING)
            .toTransportCreate().copy(responseType = "create")
    )

    @Test
    fun readModel() = testStubModel(
        "/v1/model/read",
        ModelReadRequest(),
        Context(modelResponse = ModelStub.get(), state = State.FINISHING)
            .toTransportRead().copy(responseType = "read")
    )

    @Test
    fun updateModel() = testStubModel(
        "/v1/model/update",
        ModelUpdateRequest(),
        Context(modelResponse = ModelStub.get(), state = State.FINISHING)
            .toTransportUpdate().copy(responseType = "update")
    )

    @Test
    fun deleteModel() = testStubModel(
        "/v1/model/delete",
        ModelDeleteRequest(),
        Context(modelResponse = ModelStub.get(), state = State.FINISHING)
            .toTransportDelete().copy(responseType = "delete")
    )

    @Test
    fun searchModel() = testStubModel(
        "/v1/model/search",
        ModelSearchRequest(),
        Context(
            modelsResponse = ModelStub.prepareSearchList("model search").toMutableList(),
            state = State.FINISHING
        )
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
