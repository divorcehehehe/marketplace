package rom.app.spring.repo

import org.assertj.core.api.Assertions.assertThat
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.web.reactive.function.BodyInserters
import rom.mappers.v2.*
import rom.api.v2.models.*
import rom.common.Context
import rom.common.models.*
import rom.stubs.ModelStub
import kotlin.test.Test

internal abstract class ModelRepoBaseV2Test {
    protected abstract var webClient: WebTestClient
    private val debug = ModelDebug(mode = ModelRequestDebugMode.TEST)
    protected val uuidNew = "10000000-0000-0000-0000-000000000002"

    @Test
    open fun createModel() = testRepoModel(
        "create",
        ModelCreateRequest(
            model = ModelStub.get().toTransportCreate(),
            debug = debug,
        ),
        prepareCtx(ModelStub.prepareResult {
            id = ModelId(uuidNew)
            lock = ModelLock(uuidNew)
            params = params.map { param ->
                param.copy(modelId = id, paramId = ParamId(uuidNew))
            }.toMutableList()
            ownerId = UserId("user")
        }).toTransportCreate()
    )

    @Test
    open fun readModel() = testRepoModel(
        "read",
        ModelReadRequest(
            model = ModelStub.get().toTransportRead(),
            debug = debug,
        ),
        prepareCtx(ModelStub.get())
            .toTransportRead()
    )

    @Test
    open fun updateModel() = testRepoModel(
        "update",
        ModelUpdateRequest(
            model = ModelStub.prepareResult { name = "add" }.toTransportUpdate(),
            debug = debug,
        ),
        prepareCtx(ModelStub.prepareResult {
            name = "add"
            lock = ModelLock(uuidNew)
            params = params.map { param ->
                param.copy(modelId = id, paramId = ParamId(uuidNew))
            }.toMutableList()
            ownerId = UserId("user")
        }).toTransportUpdate()
    )

    @Test
    open fun deleteModel() = testRepoModel(
        "delete",
        ModelDeleteRequest(
            model = ModelStub.get().toTransportDelete(),
            debug = debug,
        ),
        prepareCtx(ModelStub.get())
            .toTransportDelete()
    )

    @Test
    open fun searchModel() = testRepoModel(
        "search",
        ModelSearchRequest(
            modelFilter = ModelSearchFilter(
                searchString = "d-666",
                requestUserId = "user",
            ),
            debug = debug,
        ),
        Context(
            state = State.RUNNING,
            modelsResponse = ModelStub.prepareSearchList("xx")
                .sortedBy { it.id.asString() }
                .toMutableList()
        ).toTransportSearch()
    )

    @Test
    open fun trainModel() = testRepoModel(
        "train",
        ModelTrainRequest(
            model = ModelStub.get().toTransportTrain(),
            debug = debug,
        ),
        prepareCtx(ModelStub.prepareResult {
            lock = ModelLock(uuidNew)
            params = params.map { param ->
                param.copy(modelId = id, paramId = ParamId(uuidNew))
            }.toMutableList()
            ownerId = UserId("user")
            usVector = arrayOf(1.0, 2.0, 3.0)
            vtVector = arrayOf(4.0, 5.0, 6.0)
        }).toTransportTrain()
    )

    @Test
    open fun predictModel() = testRepoModel(
        "predict",
        ModelPredictRequest(
            model = ModelStub.prepareResult {
                paramValues = arrayOf(7.0)
            }.toTransportPredict(),
            debug = debug,
        ),
        prepareCtx(ModelStub.prepareResult {
            lock = ModelLock(uuidNew)
            params = params.map { param ->
                param.copy(modelId = id, paramId = ParamId(uuidNew))
            }.toMutableList()
            ownerId = UserId("user")
            usVector = arrayOf(9.0, 9.0, 9.0)
            vtVector = arrayOf(9.0, 9.0, 9.0)
            field = arrayOf(7.0, 8.0, 9.0)
        }).toTransportPredict()
    )

    private fun prepareCtx(model: Model) = Context(
        state = State.RUNNING,
        modelResponse = model,
    )

    private inline fun <reified Req : Any, reified Res : IResponse> testRepoModel(
        url: String,
        requestObj: Req,
        expectObj: Res,
    ) {
        webClient
            .post()
            .uri("/v2/model/$url")
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(requestObj))
            .exchange()
            .expectStatus().isOk
            .expectBody(Res::class.java)
            .value {
                println("RESPONSE: $it")
                val sortedResp: IResponse = when (it) {
                    is ModelSearchResponse -> it.copy(models = it.models?.sortedBy { model -> model.id })
                    else -> it
                }
                assertThat(sortedResp).isEqualTo(expectObj)
            }
    }
}
