package rom.app.rabbit.controllers

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Delivery
import rom.api.v2.apiV2RequestDeserialize
import rom.api.v2.apiV2ResponseSerialize
import rom.api.v2.models.IRequest
import rom.mappers.v2.fromTransport
import rom.mappers.v2.toTransportModel
import rom.app.common.controllerHelper
import rom.app.rabbit.config.AppSettings
import rom.common.Context
import rom.common.helpers.asError
import rom.common.models.State

class RabbitDirectControllerV2(
    private val appSettings: AppSettings,
) : RabbitProcessorBase(
    rabbitConfig = appSettings.rabbit,
    exchangeConfig = appSettings.controllersConfigV2,
    loggerProvider = appSettings.corSettings.loggerProvider,
) {

    override suspend fun Channel.processMessage(message: Delivery) {
        appSettings.controllerHelper(
            {
                val req = apiV2RequestDeserialize<IRequest>(String(message.body))
                fromTransport(req)
            },
            {
                val res = toTransportModel()
                apiV2ResponseSerialize(res).also {
                    basicPublish(exchangeConfig.exchange, exchangeConfig.keyOut, null, it.toByteArray())
                }
            },
            RabbitDirectControllerV2::class,
            "rabbitmq-v2-processor"
        )
    }

    override fun Channel.onError(e: Throwable, delivery: Delivery) {
        val context = Context()
        e.printStackTrace()
        context.state = State.FAILING
        context.errors.add(e.asError())
        val response = context.toTransportModel()
        apiV2ResponseSerialize(response).also {
            basicPublish(exchangeConfig.exchange, exchangeConfig.keyOut, null, it.toByteArray())
        }
    }
}
