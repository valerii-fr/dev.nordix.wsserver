package dev.nordix.wsserver.plugins

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
            server.updateOnConnect(ConnectedDevice(
                name = local.remoteHost,
                host = local.remoteHost,
                connectedAt = Instant.now(),
            ))

            server.setSocketCallback(call.request.local.remoteAddress) { string ->
                scope.launch { send(string) }
            }

            scope.launch {
                closeReason.await()
                server.disconnected(call.request.local.remoteHost)
                this.cancel()
            }


            for (frame in incoming) {
                when (frame) {
                    is Frame.Text -> {
                        val text = frame.readText()
                        when (text) {
                            "1.0" -> server.toggleToNoCallback(false)
                            "1.1" -> server.toggleToNoCallback(true)
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
