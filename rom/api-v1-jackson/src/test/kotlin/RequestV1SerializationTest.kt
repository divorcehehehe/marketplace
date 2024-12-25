package rom.api.v1

import rom.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class RequestV1SerializationTest {
    private val request = ModelCreateRequest(
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
            sampling = ModelSampling.LATIN_HYPER_CUBE,
            visibility = ModelVisibility.PUBLIC,
        )
    )

    @Test
    fun serialize() {
        val json = apiV1Mapper.writeValueAsString(request)

        assertContains(json, "\"name\":\"model\"")
        assertContains(json, "\"macroPath\":\"path/to/macro\"")
        assertContains(json, "\"solverPath\":\"path/to/solver\"")
        assertContains(json, "\"sampling\":\"latinHyperCube\"")
        assertContains(json, "\"requestType\":\"create\"")
        assertContains(json, "\"mode\":\"stub\"")
        assertContains(json, "\"stub\":\"badName\"")
    }

    @Test
    fun serializeNaked() {
        val json = apiV1Mapper.writeValueAsString(ModelCreateRequest())
        assertContains(json, "\"model\":null")
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(request)
        val obj = apiV1Mapper.readValue(json, IRequest::class.java) as ModelCreateRequest

        assertEquals(request, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {"model": null}
        """.trimIndent()
        val obj = apiV1Mapper.readValue(jsonString, ModelCreateRequest::class.java)

        assertEquals(null, obj.model)
    }
}
