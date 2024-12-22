package rom.app.spring.controllers

import org.springframework.web.bind.annotation.*
import rom.app.spring.config.AppSettings
import rom.mappers.v2.*
import rom.api.v2.models.*
import rom.app.common.controllerHelper
import kotlin.reflect.KClass

@Suppress("unused")
@RestController
@RequestMapping("v2/model")
class ModelControllerV2Fine(private val appSettings: AppSettings) {

    @PostMapping("create")
    suspend fun create(@RequestBody request: ModelCreateRequest): ModelCreateResponse =
        process(appSettings, request = request, this::class, "create")

    @PostMapping("read")
    suspend fun  read(@RequestBody request: ModelReadRequest): ModelReadResponse =
        process(appSettings, request = request, this::class, "read")

    @RequestMapping("update", method = [RequestMethod.POST])
    suspend fun  update(@RequestBody request: ModelUpdateRequest): ModelUpdateResponse =
        process(appSettings, request = request, this::class, "update")

    @PostMapping("delete")
    suspend fun  delete(@RequestBody request: ModelDeleteRequest): ModelDeleteResponse =
        process(appSettings, request = request, this::class, "delete")

    @PostMapping("search")
    suspend fun  search(@RequestBody request: ModelSearchRequest): ModelSearchResponse =
        process(appSettings, request = request, this::class, "search")

    @PostMapping("train")
    suspend fun  search(@RequestBody request: ModelTrainRequest): ModelTrainResponse =
        process(appSettings, request = request, this::class, "train")

    @PostMapping("predict")
    suspend fun  search(@RequestBody request: ModelPredictRequest): ModelPredictResponse =
        process(appSettings, request = request, this::class, "predict")

    companion object {
        suspend inline fun <reified Q : IRequest, reified R : IResponse> process(
            appSettings: AppSettings,
            request: Q,
            clazz: KClass<*>,
            logId: String,
        ): R = appSettings.controllerHelper(
            {
                fromTransport(request)
            },
            { toTransportModel() as R },
            clazz,
            logId,
        )
    }
}
