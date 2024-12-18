@file:Suppress("unused")

package rom.api.v2

import kotlinx.serialization.json.Json
import rom.api.v2.models.IRequest
import rom.api.v2.models.IResponse

@Suppress("JSON_FORMAT_REDUNDANT_DEFAULT")
val apiV2Mapper = Json {}

@Suppress("unused")
fun apiV2RequestSerialize(obj: IRequest): String =
    apiV2Mapper.encodeToString(IRequest.serializer(), obj)

@Suppress("UNCHECKED_CAST")
fun <T : IRequest> apiV2RequestDeserialize(json: String) =
    apiV2Mapper.decodeFromString<IRequest>(json) as T

@Suppress("unused")
fun apiV2ResponseSerialize(obj: IResponse): String =
    apiV2Mapper.encodeToString(IResponse.serializer(), obj)

@Suppress("UNCHECKED_CAST")
fun <T : IResponse> apiV2ResponseDeserialize(json: String) =
    apiV2Mapper.decodeFromString<IResponse>(json) as T
