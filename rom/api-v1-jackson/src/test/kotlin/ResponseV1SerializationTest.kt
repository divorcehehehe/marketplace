package rom.api.v1
import rom.api.v1.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseV1SerializationTest {
    private val response = ModelCreateResponse(
        model = ModelResponseObject(
            name = "model",
            macroPath = "path/to/macro",
            solverPath = "path/to/solver",
            params = listOf(
                ParamResponseObject(
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
        val json = apiV1Mapper.writeValueAsString(response)

        assertContains(json, "\"name\":\"model\"")
        assertContains(json, "\"macroPath\":\"path/to/macro\"")
        assertContains(json, "\"solverPath\":\"path/to/solver\"")
        assertContains(json, "\"sampling\":\"latinHyperCube\"")
        assertContains(json, "\"responseType\":\"create\"")
    }

    @Test
    fun serializeNaked() {
        val json = apiV1Mapper.writeValueAsString(ModelCreateResponse())
        assertContains(json, "\"model\":null")
    }

    @Test
    fun deserialize() {
        val json = apiV1Mapper.writeValueAsString(response)
        val obj = apiV1Mapper.readValue(json, IResponse::class.java) as ModelCreateResponse

        assertEquals(response, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {"model": null}
        """.trimIndent()
        val obj = apiV1Mapper.readValue(jsonString, ModelCreateResponse::class.java)

        assertEquals(null, obj.model)
    }
}