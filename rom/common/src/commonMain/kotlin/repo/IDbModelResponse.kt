package rom.common.repo

import rom.common.models.Model
import rom.common.models.ROMError

sealed interface IDbModelResponse: IDbResponse<Model>

data class DbModelResponseOk(
    val data: Model
): IDbModelResponse

data class DbModelResponseErr(
    val errors: List<ROMError> = emptyList()
): IDbModelResponse {
    constructor(err: ROMError): this(listOf(err))
}

data class DbModelResponseErrWithData(
    val data: Model,
    val errors: List<ROMError> = emptyList()
): IDbModelResponse {
    constructor(model: Model, err: ROMError): this(model, listOf(err))
}
