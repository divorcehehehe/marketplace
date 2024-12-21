package rom.app.rabbit

import com.rabbitmq.client.CancelCallback
import com.rabbitmq.client.ConnectionFactory
import com.rabbitmq.client.DeliverCallback
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.AfterClass
import org.junit.BeforeClass
import org.testcontainers.containers.RabbitMQContainer
import rom.api.v1.apiV1Mapper
import rom.api.v1.models.ModelCreateObject
import rom.api.v1.models.ModelCreateRequest
import rom.api.v1.models.ModelCreateResponse
import rom.api.v1.models.ModelDebug
import rom.api.v1.models.ModelRequestDebugMode
import rom.api.v1.models.ModelRequestDebugStubs
import rom.api.v1.models.BaseParam
import rom.api.v1.models.ModelSampling
import rom.api.v1.models.ModelVisibility
import rom.api.v2.apiV2RequestSerialize
import rom.api.v2.apiV2ResponseDeserialize
import rom.app.rabbit.config.AppSettings
import rom.app.rabbit.config.RabbitConfig
import rom.app.rabbit.config.RabbitExchangeConfiguration
import rom.stubs.ModelStub
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import rom.api.v2.models.ModelCreateObject as ModelCreateObjectV2
import rom.api.v2.models.ModelCreateRequest as ModelCreateRequestV2
import rom.api.v2.models.ModelCreateResponse as ModelCreateResponseV2
import rom.api.v2.models.ModelDebug as ModelDebugV2
import rom.api.v2.models.ModelRequestDebugMode as ModelRequestDebugModeV2
import rom.api.v2.models.ModelRequestDebugStubs as ModelRequestDebugStubsV2
import rom.api.v2.models.BaseParam as BaseParamV2
import rom.api.v2.models.ModelSampling as ModelSamplingV2
import rom.api.v2.models.ModelVisibility as ModelVisibilityV2

//  тесты с использованием testcontainers
internal class RabbitMqTest {

    companion object {
        const val EXCHANGE = "test-exchange"
        const val EXCHANGE_TYPE = "direct"
        const val RMQ_PORT = 5672

        private val container = run {
//            Этот образ предназначен для дебагинга, он содержит панель управления на порту httpPort
//            RabbitMQContainer("rabbitmq:3-management").apply {
//            Этот образ минимальный и не содержит панель управления
            RabbitMQContainer("rabbitmq:latest").apply {
//                withExposedPorts(5672, 15672) // Для 3-management
                withExposedPorts(RMQ_PORT)
            }
        }

        @BeforeClass
        @JvmStatic
        fun beforeAll() {
            container.start()
//            println("CONTAINER PORT (15672): ${container.getMappedPort(15672)}")
        }

        @AfterClass
        @JvmStatic
        fun afterAll() {
            container.stop()
        }
    }

    private val appSettings = AppSettings(
        rabbit = RabbitConfig(
            port = container.getMappedPort(RMQ_PORT)
        ),
//        corSettings = MkplCorSettings(loggerProvider = MpLoggerProvider { mpLoggerLogback(it) }),
        controllersConfigV1 = RabbitExchangeConfiguration(
            keyIn = "in-v1",
            keyOut = "out-v1",
            exchange = EXCHANGE,
            queue = "v1-queue",
            consumerTag = "v1-consumer-test",
            exchangeType = EXCHANGE_TYPE
        ),
        controllersConfigV2 = RabbitExchangeConfiguration(
            keyIn = "in-v2",
            keyOut = "out-v2",
            exchange = EXCHANGE,
            queue = "v2-queue",
            consumerTag = "v2-consumer-test",
            exchangeType = EXCHANGE_TYPE
        ),
    )
    private val app = RabbitApp(appSettings = appSettings)

    @BeforeTest
    fun tearUp() {
        app.start()
    }

    @AfterTest
    fun tearDown() {
        println("Test is being stopped")
        app.close()
    }

    @Test
    fun adCreateTestV1() {
        val (keyOut, keyIn) = with(appSettings.controllersConfigV1) { Pair(keyOut, keyIn) }
        val (tstHost, tstPort) = with(appSettings.rabbit) { Pair(host, port) }
        ConnectionFactory().apply {
            host = tstHost
            port = tstPort
            username = "guest"
            password = "guest"
        }.newConnection().use { connection ->
            connection.createChannel().use { channel ->
                var responseJson = ""
                channel.exchangeDeclare(EXCHANGE, "direct")
                val queueOut = channel.queueDeclare().queue
                channel.queueBind(queueOut, EXCHANGE, keyOut)
                val deliverCallback = DeliverCallback { consumerTag, delivery ->
                    responseJson = String(delivery.body, Charsets.UTF_8)
                    println(" [x] Received by $consumerTag: '$responseJson'")
                }
                channel.basicConsume(queueOut, true, deliverCallback, CancelCallback { })

                channel.basicPublish(EXCHANGE, keyIn, null, apiV1Mapper.writeValueAsBytes(boltCreateV1))

                runBlocking {
                    withTimeoutOrNull(1000L) {
                        while (responseJson.isBlank()) {
                            delay(10)
                        }
                    }
                }

                println("RESPONSE: $responseJson")
                val response = apiV1Mapper.readValue(responseJson, ModelCreateResponse::class.java)
                val expected = ModelStub.get()

                assertEquals(expected.name, response.model?.name)
                assertEquals(expected.macroPath, response.model?.macroPath)
                assertEquals(expected.solverPath, response.model?.solverPath)
                assertEquals(expected.params.firstOrNull()?.line, response.model?.params?.firstOrNull()?.line)
                assertEquals(expected.params.firstOrNull()?.position, response.model?.params?.firstOrNull()?.position)
                assertEquals(expected.params.firstOrNull()?.separator, response.model?.params?.firstOrNull()?.separator)
                assertEquals(expected.params.firstOrNull()?.name, response.model?.params?.firstOrNull()?.name)
                assertEquals(expected.params.firstOrNull()?.units, response.model?.params?.firstOrNull()?.units)
                assertEquals(expected.params.firstOrNull()?.bounds, response.model?.params?.firstOrNull()?.bounds)
                assertEquals(expected.sampling.name, response.model?.sampling?.name)
            }
        }
    }

    @Test
    fun adCreateTestV2() {
        val (keyOut, keyIn) = with(appSettings.controllersConfigV2) { Pair(keyOut, keyIn) }
        val (tstHost, tstPort) = with(appSettings.rabbit) { Pair(host, port) }
        ConnectionFactory().apply {
            host = tstHost
            port = tstPort
            username = "guest"
            password = "guest"
        }.newConnection().use { connection ->
            connection.createChannel().use { channel ->
                var responseJson = ""
                channel.exchangeDeclare(EXCHANGE, "direct")
                val queueOut = channel.queueDeclare().queue
                channel.queueBind(queueOut, EXCHANGE, keyOut)
                val deliverCallback = DeliverCallback { consumerTag, delivery ->
                    responseJson = String(delivery.body, Charsets.UTF_8)
                    println(" [x] Received by $consumerTag: '$responseJson'")
                }
                channel.basicConsume(queueOut, true, deliverCallback, CancelCallback { })

                channel.basicPublish(EXCHANGE, keyIn, null, apiV2RequestSerialize(boltCreateV2).toByteArray())

                runBlocking {
                    withTimeoutOrNull(1000L) {
                        while (responseJson.isBlank()) {
                            delay(10)
                        }
                    }
                }

                println("RESPONSE: $responseJson")
                val response = apiV2ResponseDeserialize<ModelCreateResponseV2>(responseJson)
                val expected = ModelStub.get()
                assertEquals(expected.name, response.model?.name)
                assertEquals(expected.macroPath, response.model?.macroPath)
                assertEquals(expected.solverPath, response.model?.solverPath)
                assertEquals(expected.params.firstOrNull()?.line, response.model?.params?.firstOrNull()?.line)
                assertEquals(expected.params.firstOrNull()?.position, response.model?.params?.firstOrNull()?.position)
                assertEquals(expected.params.firstOrNull()?.separator, response.model?.params?.firstOrNull()?.separator)
                assertEquals(expected.params.firstOrNull()?.name, response.model?.params?.firstOrNull()?.name)
                assertEquals(expected.params.firstOrNull()?.units, response.model?.params?.firstOrNull()?.units)
                assertEquals(expected.params.firstOrNull()?.bounds, response.model?.params?.firstOrNull()?.bounds)
                assertEquals(expected.sampling.name, response.model?.sampling?.name)
            }
        }
    }

    private val boltCreateV1 = with(ModelStub.get()) {
        ModelCreateRequest(
            debug = ModelDebug(
                mode = ModelRequestDebugMode.STUB,
                stub = ModelRequestDebugStubs.SUCCESS,
            ),
            model = ModelCreateObject(
                name = "Обтекание крыла",
                macroPath = "путь/к/макросу",
                solverPath = "путь/к/солверу",
                params = listOf(
                    BaseParam(
                        line = 1,
                        position = 2,
                        separator = "=",
                        name = "Скорость",
                        units = "м/с",
                        bounds = mutableListOf(100.0, 200.0),
                    ),
                ),
                sampling = ModelSampling.ADAPTIVE_SAMPLING,
                visibility = ModelVisibility.PUBLIC,
            ),
        )
    }

    private val boltCreateV2 = with(ModelStub.get()) {
        ModelCreateRequestV2(
            debug = ModelDebugV2(
                mode = ModelRequestDebugModeV2.STUB,
                stub = ModelRequestDebugStubsV2.SUCCESS,
            ),
            model = ModelCreateObjectV2(
                name = "Обтекание крыла",
                macroPath = "путь/к/макросу",
                solverPath = "путь/к/солверу",
                params = listOf(
                    BaseParamV2(
                        line = 1,
                        position = 2,
                        separator = "=",
                        name = "Скорость",
                        units = "м/с",
                        bounds = mutableListOf(100.0, 200.0),
                    ),
                ),
                sampling = ModelSamplingV2.ADAPTIVE_SAMPLING,
                visibility = ModelVisibilityV2.PUBLIC,
            ),
        )
    }
}
