package rom.common.repo

import rom.common.models.Model
import rom.common.models.ROMError

sealed interface IDbModelsResponse: IDbResponse<List<Model>>

data class DbModelsResponseOk(
    val data: List<Model>
): IDbModelsResponse
@Suppress("unused")

data class DbModelsResponseErr(
    val errors: List<ROMError> = emptyList()
): IDbModelsResponse {
    constructor(err: ROMError): this(listOf(err))
}
