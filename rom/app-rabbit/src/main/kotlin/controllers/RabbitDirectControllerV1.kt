package rom.app.rabbit.controllers

import com.rabbitmq.client.Channel
import com.rabbitmq.client.Delivery
import rom.api.v1.apiV1Mapper

import rom.api.v1.models.IRequest
import rom.mappers.v1.fromTransport
import rom.mappers.v1.toTransportModel
import rom.app.common.controllerHelper
import rom.app.rabbit.config.AppSettings
import rom.common.Context
import rom.common.helpers.asError
import rom.common.models.State

class RabbitDirectControllerV1(
    private val appSettings: AppSettings,
) : RabbitProcessorBase(
    rabbitConfig = appSettings.rabbit,
    exchangeConfig = appSettings.controllersConfigV1,
    loggerProvider = appSettings.corSettings.loggerProvider,
) {
    override suspend fun Channel.processMessage(message: Delivery) {
        appSettings.controllerHelper(
            {
                val req = apiV1Mapper.readValue(message.body, IRequest::class.java)
                fromTransport(req)
            },
            {
                val res = toTransportModel()
                apiV1Mapper.writeValueAsBytes(res).also {
                    basicPublish(exchangeConfig.exchange, exchangeConfig.keyOut, null, it)
                }
            },
            this@RabbitDirectControllerV1::class,
            "rabbitmq-v1-processor"
        )
    }

    override fun Channel.onError(e: Throwable, delivery: Delivery) {
        val context = Context()
        e.printStackTrace()
        context.state = State.FAILING
        context.errors.add(e.asError())
        val response = context.toTransportModel()
        apiV1Mapper.writeValueAsBytes(response).also {
            basicPublish(exchangeConfig.exchange, exchangeConfig.keyOut, null, it)
        }
    }
}
