package rom.backend.repo.postgresql

import kotlinx.serialization.Serializable

@Serializable
data class JsonParams(
    val params: List<JsonParam>
)

@Serializable
data class JsonParam(
    val line: Int,
    val position: Int,
    val separator: String,
    val name: String,
    val units: String,
    val bounds: List<Double>,
    var paramId: String,
    var modelId: String,
)
