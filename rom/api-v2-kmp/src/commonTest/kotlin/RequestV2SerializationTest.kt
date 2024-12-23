package rom.api.v2

import kotlinx.serialization.encodeToString
import rom.api.v2.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestV2SerializationTest {
    private val request: IRequest = ModelCreateRequest(
        debug = ModelDebug(
            mode = ModelRequestDebugMode.STUB,
            stub = ModelRequestDebugStubs.BAD_NAME
        ),
        model = ModelCreateObject(
            name = "model",
            macroPath = "path/to/macro",
            solverPath = "path/to/solver",
            params = listOf(
                BaseParam(
                    line = 1,
                    position = 2,
                    separator = "=",
                    name = "Temperature",
                    units = "°C",
                    bounds = listOf(0.0, 1.0)
                )
            ),
            sampling = ModelSampling.ADAPTIVE_SAMPLING,
            visibility = ModelVisibility.PUBLIC,
        )
    )

    @Test
    fun serialize() {
        val json = apiV2Mapper.encodeToString(IRequest.serializer(), request)

        assertContains(json, "\"name\":\"model\"")
        assertContains(json, "\"macroPath\":\"path/to/macro\"")
        assertContains(json, "\"solverPath\":\"path/to/solver\"")
        assertContains(json, "\"sampling\":\"adaptiveSampling\"")
        assertContains(json, "\"requestType\":\"create\"")
        assertContains(json, "\"mode\":\"stub\"")
        assertContains(json, "\"stub\":\"badName\"")
    }

    @Test
    fun deserialize() {
        val json = apiV2Mapper.encodeToString(request)
        val obj = apiV2Mapper.decodeFromString<IRequest>(json) as ModelCreateRequest

        assertEquals(request, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {"model": null}
        """.trimIndent()
        val obj = apiV2Mapper.decodeFromString<ModelCreateRequest>(jsonString)

        assertEquals(null, obj.model)
    }
}
