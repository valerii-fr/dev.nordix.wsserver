package dev.nordix.wsserver.plugins

import dev.nordix.wsserver.devices.DeviceAction
import dev.nordix.wsserver.devices.DeviceType
import dev.nordix.wsserver.devices.MessageTypes
import dev.nordix.wsserver.helpers.JsonHelper.json
import dev.nordix.wsserver.server.CommonServer
import dev.nordix.wsserver.server.model.Message
import dev.nordix.wsserver.server.MessageRepo
import dev.nordix.wsserver.server.model.ConnectedDevice
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.koin.ktor.ext.inject
import java.time.Duration
import java.time.Instant

fun Application.configureSockets() {
    val messageRepo by inject<MessageRepo>()
    val server by inject<CommonServer>()
    val scope by inject<CoroutineScope>()

    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(3)
        timeout = Duration.ofSeconds(5)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }

    routing {
        webSocket("/ws") {
            val local = call.request.local

            server.setSocketCallback(local.remoteAddress) { string ->
                scope.launch {
                    println("sending from callback $string")
                    send(string)
                }
            }

            scope.launch {
                closeReason.await()
                server.disconnected(local.remoteHost)
                this.cancel()
            }


            for (frame in incoming) {
                when (frame) {
                    is Frame.Text -> {
                        val text = frame.readText()

                        val parsedJson = json.parseToJsonElement(text)
                        when(parsedJson.jsonObject["type"]?.jsonPrimitive?.content) {
                            MessageTypes.Action.typeName -> {
                                server.actByDevice(
                                    DeviceAction(
                                        author = local.remoteHost,
                                        code = parsedJson.jsonObject["code"]?.jsonPrimitive?.content
                                            ?: throw IllegalArgumentException("json contains no act code")
                                    )
                                )
                            }
                            MessageTypes.Presentation.typeName -> {
                                val cd = ConnectedDevice(
                                    name = parsedJson.jsonObject["name"]?.jsonPrimitive?.content ?: local.remoteHost,
                                    host = local.remoteHost,
                                    connectedAt = Instant.now(),
                                    type = when(parsedJson.jsonObject["device_type"]?.jsonPrimitive?.content) {
                                        DeviceType.Button.typeName -> DeviceType.Button
                                        null -> throw IllegalArgumentException("json contains no device type")
                                        else -> throw UnsupportedOperationException("unsupported device type")
                                    }
                                )
                                println("presented $cd")
                                server.connected(cd)
                            }
                            null -> throw IllegalArgumentException("json contains no message type parameter")
                            else -> throw UnsupportedOperationException("no such element in known list")
                        }

                        messageRepo.receive(
                            Message(
                                from = call.request.local.remoteHost,
                                text = text,
                                time = Instant.now(),
                            )
                        )
                    }

                    else -> { }
                }
            }
        }
    }
}
