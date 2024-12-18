package rom.api.v2
import kotlinx.serialization.encodeToString
import rom.api.v2.models.*
import kotlin.test.Test
import kotlin.test.assertContains
import kotlin.test.assertEquals

class ResponseV2SerializationTest {
    private val response: IResponse = ModelCreateResponse(
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
            sampling = ModelSampling.ADAPTIVE_SAMPLING,
            visibility = ModelVisibility.PUBLIC,
        )
    )

    @Test
    fun serialize() {
        val json = apiV2Mapper.encodeToString(response)

        println(json)

        assertContains(json, "\"name\":\"model\"")
        assertContains(json, "\"macroPath\":\"path/to/macro\"")
        assertContains(json, "\"solverPath\":\"path/to/solver\"")
        assertContains(json, "\"sampling\":\"adaptiveSampling\"")
        assertContains(json, "\"responseType\":\"create\"")
    }

    @Test
    fun deserialize() {
        val json = apiV2Mapper.encodeToString(response)
        val obj = apiV2Mapper.decodeFromString<IResponse>(json) as ModelCreateResponse

        assertEquals(response, obj)
    }

    @Test
    fun deserializeNaked() {
        val jsonString = """
            {"model": null}
        """.trimIndent()
        val obj = apiV2Mapper.decodeFromString<ModelCreateResponse>(jsonString)

        assertEquals(null, obj.model)
    }
}