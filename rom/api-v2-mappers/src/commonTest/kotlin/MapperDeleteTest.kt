package rom.mappers.v2

import rom.api.v2.models.*
import rom.common.Context
import rom.common.exceptions.UnknownCommand
import rom.common.models.*
import kotlin.test.Test
import kotlin.test.assertEquals

class MapperDeleteTest {
    @Test
    fun fromTransport() {
        val request = ModelDeleteRequest(
            debug = ModelDebug(
                mode = ModelRequestDebugMode.STUB,
                stub = ModelRequestDebugStubs.SUCCESS,
            ),
            model = ModelDeleteObject(
                id = "model_id",
                lock = "model_lock"
            ),
        )

        val context = Context()
        context.fromTransport(request)
        assertEquals("DELETE",     context.command.name)
        assertEquals("NONE",       context.state.name)
        assertEquals(null,         context.errors.firstOrNull())
        assertEquals("STUB",       context.workMode.name)
        assertEquals("SUCCESS",    context.stubCase.name)
        assertEquals("",           context.requestId.asString())
        assertEquals("model_id",   context.modelRequest.id.asString())
        assertEquals("model_lock", context.modelRequest.lock.asString())
        assertEquals("",           context.modelRequest.name)
        assertEquals("",           context.modelRequest.macroPath)
        assertEquals("",           context.modelRequest.solverPath)
        assertEquals(null,         context.modelRequest.params.firstOrNull())
        assertEquals("NONE",       context.modelRequest.sampling.name)
        assertEquals("NONE",       context.modelRequest.visibility.name)
    }

    @Test
    fun fromTransportNecked() {
        val request = ModelDeleteRequest()

        val context = Context()
        context.fromTransport(request)
        assertEquals("DELETE", context.command.name)
        assertEquals("NONE",   context.state.name)
        assertEquals(null,     context.errors.firstOrNull())
        assertEquals("PROD",   context.workMode.name)
        assertEquals("NONE",   context.stubCase.name)
        assertEquals("",       context.requestId.asString())
        assertEquals("",       context.modelRequest.id.asString())
        assertEquals("",       context.modelRequest.lock.asString())
        assertEquals("",       context.modelRequest.name)
        assertEquals("",       context.modelRequest.macroPath)
        assertEquals("",       context.modelRequest.solverPath)
        assertEquals(null,     context.modelRequest.params.firstOrNull())
        assertEquals("NONE",   context.modelRequest.sampling.name)
        assertEquals("NONE",   context.modelRequest.visibility.name)
    }

    @Test
    fun toTransport() {
        val context = Context(
            command = Command.DELETE,
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
            modelResponse = Model(
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
        )

        val request = context.toTransportModel() as ModelDeleteResponse

        assertEquals("success",          request.result?.value)
        assertEquals("err",              request.errors?.firstOrNull()?.code)
        assertEquals("request",          request.errors?.firstOrNull()?.group)
        assertEquals("name",             request.errors?.firstOrNull()?.field)
        assertEquals("wrong name",       request.errors?.firstOrNull()?.message)
        assertEquals("model_id",         request.model?.id)
        assertEquals("owner_id",         request.model?.ownerId)
        assertEquals("model_lock",       request.model?.lock)
        assertEquals("delete",           request.model?.permissions?.firstOrNull()?.value)
        assertEquals(null,               request.model?.field)
        assertEquals("model",            request.model?.name)
        assertEquals("path/to/macro",    request.model?.macroPath)
        assertEquals("path/to/solver",   request.model?.solverPath)
        assertEquals(1,                  request.model?.params?.firstOrNull()?.line)
        assertEquals(2,                  request.model?.params?.firstOrNull()?.position)
        assertEquals("=",                request.model?.params?.firstOrNull()?.separator)
        assertEquals("Temperature",      request.model?.params?.firstOrNull()?.name)
        assertEquals("°C",               request.model?.params?.firstOrNull()?.units)
        assertEquals(0.0,                request.model?.params?.firstOrNull()?.bounds?.firstOrNull())
        assertEquals(1.0,                request.model?.params?.firstOrNull()?.bounds?.lastOrNull())
        assertEquals("param_id",         request.model?.params?.firstOrNull()?.id)
        assertEquals("model_id",         request.model?.params?.firstOrNull()?.modelId)
        assertEquals("adaptiveSampling", request.model?.sampling?.value)
        assertEquals("public",           request.model?.visibility?.value)
        assertEquals(null,               request.model?.paramValues)
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
            command = Command.DELETE,
        )

        val request = context.toTransportModel() as ModelDeleteResponse

        assertEquals(null,     request.result)
        assertEquals(null,     request.errors)
        assertEquals(null,     request.model?.id)
        assertEquals(null,     request.model?.ownerId)
        assertEquals(null,     request.model?.lock)
        assertEquals(null,     request.model?.permissions)
        assertEquals(null,     request.model?.field)
        assertEquals(null,     request.model?.name)
        assertEquals(null,     request.model?.macroPath)
        assertEquals(null,     request.model?.solverPath)
        assertEquals(null,     request.model?.params)
        assertEquals(null,     request.model?.sampling)
        assertEquals(null,     request.model?.visibility)
        assertEquals(null,     request.model?.paramValues)
    }
}
