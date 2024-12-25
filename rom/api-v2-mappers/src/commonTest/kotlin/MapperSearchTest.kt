package rom.mappers.v2

import rom.api.v2.models.*
import rom.common.Context
import rom.common.exceptions.UnknownCommand
import rom.common.models.*
import kotlin.test.Test
import kotlin.test.assertEquals

class MapperSearchTest {
    @Test
    fun fromTransport() {
        val request = ModelSearchRequest(
            debug = ModelDebug(
                mode = ModelRequestDebugMode.STUB,
                stub = ModelRequestDebugStubs.SUCCESS,
            ),
            modelFilter = ModelSearchFilter(
                searchString = "model",
            ),
            requestUserId = "user_id"
        )

        val context = Context()
        context.fromTransport(request)
        assertEquals("SEARCH",  context.command.name)
        assertEquals("NONE",    context.state.name)
        assertEquals(null,      context.errors.firstOrNull())
        assertEquals("STUB",    context.workMode.name)
        assertEquals("SUCCESS", context.stubCase.name)
        assertEquals("",        context.requestId.asString())
        assertEquals("model",   context.modelFilterRequest.searchString)
        assertEquals("",        context.modelFilterRequest.ownerId.asString())
        assertEquals("user_id", context.requestUserId.asString())
    }

    @Test
    fun fromTransportNecked() {
        val request = ModelSearchRequest()

        val context = Context()
        context.fromTransport(request)
        assertEquals("SEARCH", context.command.name)
        assertEquals("NONE",   context.state.name)
        assertEquals(null,     context.errors.firstOrNull())
        assertEquals("PROD",   context.workMode.name)
        assertEquals("NONE",   context.stubCase.name)
        assertEquals("",       context.requestId.asString())
        assertEquals("",       context.modelFilterRequest.searchString)
        assertEquals("",       context.modelFilterRequest.ownerId.asString())
        assertEquals("",       context.requestUserId.asString())
    }

    @Test
    fun toTransport() {
        val context = Context(
            command = Command.SEARCH,
            state = State.RUNNING,
            errors = mutableListOf(
                ROMError(
                    code = "err",
                    group = "request",
                    field = "name",
                    message = "wrong name",
                ),
            ),
            requestId = RequestId("1234"),
            modelsResponse = mutableListOf(
                Model(
                    id = ModelId("model_id"),
                    ownerId = UserId("owner_id"),
                    lock = ModelLock("model_lock"),
                    permissionsClient = mutableSetOf(ModelPermissionClient.DELETE),
                    name = "model",
                    macroPath = "path/to/macro",
                    solverPath = "path/to/solver",
                    params = mutableListOf(
                        Param(
                            line = 1,
                            position = 2,
                            separator = "=",
                            name = "Temperature",
                            units = "°C",
                            bounds = mutableListOf(0.0, 1.0),
                            paramId = ParamId("param_id"),
                            modelId = ModelId("model_id"),
                        ),
                    ),
                    sampling = Sampling.ADAPTIVE_SAMPLING,
                    visibility = Visibility.VISIBLE_PUBLIC,
                ),
            ),
        )

        val request = context.toTransportModel() as ModelSearchResponse

        assertEquals("success",          request.result?.value)
        assertEquals("err",              request.errors?.firstOrNull()?.code)
        assertEquals("request",          request.errors?.firstOrNull()?.group)
        assertEquals("name",             request.errors?.firstOrNull()?.field)
        assertEquals("wrong name",       request.errors?.firstOrNull()?.message)
        assertEquals("model_id",         request.models?.firstOrNull()?.id)
        assertEquals("owner_id",         request.models?.firstOrNull()?.ownerId)
        assertEquals("model_lock",       request.models?.firstOrNull()?.lock)
        assertEquals("delete",           request.models?.firstOrNull()?.permissions?.firstOrNull()?.value)
        assertEquals(null,               request.models?.firstOrNull()?.field)
        assertEquals("model",            request.models?.firstOrNull()?.name)
        assertEquals("path/to/macro",    request.models?.firstOrNull()?.macroPath)
        assertEquals("path/to/solver",   request.models?.firstOrNull()?.solverPath)
        assertEquals(1,                  request.models?.firstOrNull()?.params?.firstOrNull()?.line)
        assertEquals(2,                  request.models?.firstOrNull()?.params?.firstOrNull()?.position)
        assertEquals("=",                request.models?.firstOrNull()?.params?.firstOrNull()?.separator)
        assertEquals("Temperature",      request.models?.firstOrNull()?.params?.firstOrNull()?.name)
        assertEquals("°C",               request.models?.firstOrNull()?.params?.firstOrNull()?.units)
        assertEquals(0.0,                request.models?.firstOrNull()?.params?.firstOrNull()?.bounds?.firstOrNull())
        assertEquals(1.0,                request.models?.firstOrNull()?.params?.firstOrNull()?.bounds?.lastOrNull())
        assertEquals("param_id",         request.models?.firstOrNull()?.params?.firstOrNull()?.id)
        assertEquals("model_id",         request.models?.firstOrNull()?.params?.firstOrNull()?.modelId)
        assertEquals("adaptiveSampling", request.models?.firstOrNull()?.sampling?.value)
        assertEquals("public",           request.models?.firstOrNull()?.visibility?.value)
        assertEquals(null,               request.models?.firstOrNull()?.paramValues)
    }

    @Test
    fun toTransportNecked() {
        // fully necked
        try {
            val context = Context()

            context.toTransportModel()
        } catch (exception: UnknownCommand) {
            assertEquals("Wrong command NONE at mapping toTransport stage", exception.message)
        }

        // necked with command
        val context = Context(
            command = Command.SEARCH,
        )

        val request = context.toTransportModel() as ModelSearchResponse

        assertEquals(null,     request.result)
        assertEquals(null,     request.errors)
        assertEquals(null,     request.models)
    }
}
