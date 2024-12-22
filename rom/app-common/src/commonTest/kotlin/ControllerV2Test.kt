package rom.app.common

import kotlinx.coroutines.test.runTest
import rom.mappers.v2.fromTransport
import rom.mappers.v2.toTransportModel
import rom.api.v2.models.*
import rom.biz.ModelProcessor
import rom.common.CorSettings
import kotlin.test.Test
import kotlin.test.assertEquals

class ControllerV2Test {
    private val request = ModelCreateRequest(
        model = ModelCreateObject(
            name = "model name",
            macroPath = "path/to/macro",
            solverPath = "path/to/solver",
            params = listOf(
                BaseParam(
                    line = 1,
                    position = 2,
                    separator = "=",
                    name = "Temperature",
                    units = "°C",
                    bounds = listOf(0.0, 1.0),
                ),
            ),
            sampling = ModelSampling.ADAPTIVE_SAMPLING,
            visibility = ModelVisibility.PUBLIC,
        ),
        debug = ModelDebug(mode = ModelRequestDebugMode.STUB, stub = ModelRequestDebugStubs.SUCCESS)
    )

    private val appSettings: IAppSettings = object : IAppSettings {
        override val corSettings: CorSettings = CorSettings()
        override val processor: ModelProcessor = ModelProcessor(corSettings)
    }

    private suspend fun createModelSpring(request: ModelCreateRequest): ModelCreateResponse =
        appSettings.controllerHelper(
            { fromTransport(request) },
            { toTransportModel() as ModelCreateResponse },
            ControllerV2Test::class,
            "controller-v2-test"
        )

    class TestApplicationCall(private val request: IRequest) {
        var res: IResponse? = null

        @Suppress("UNCHECKED_CAST")
        fun <T : IRequest> receive(): T = request as T
        fun respond(res: IResponse) {
            this.res = res
        }
    }

    private suspend fun TestApplicationCall.createModelKtor(appSettings: IAppSettings) {
        val resp = appSettings.controllerHelper(
            { fromTransport(receive<ModelCreateRequest>()) },
            { toTransportModel() },
            ControllerV2Test::class,
            "controller-v2-test"
        )
        respond(resp)
    }

    @Test
    fun springHelperTest() = runTest {
        val res = createModelSpring(request)
        assertEquals(ResponseResult.SUCCESS, res.result)
    }

    @Test
    fun ktorHelperTest() = runTest {
        val testApp = TestApplicationCall(request).apply { createModelKtor(appSettings) }
        val res = testApp.res as ModelCreateResponse
        assertEquals(ResponseResult.SUCCESS, res.result)
    }
}
