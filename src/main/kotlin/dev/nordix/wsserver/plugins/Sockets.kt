package dev.nordix.wsserver.plugins

import dev.nordix.wsserver.helpers.LogHelper
import dev.nordix.wsserver.server.CommonServer
import dev.nordix.wsserver.server.Message
import dev.nordix.wsserver.server.MessageRepo
import io.ktor.server.application.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import io.ktor.websocket.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.koin.ktor.ext.inject
import org.koin.logger.SLF4JLogger
import java.time.Duration
import java.time.Instant

fun Application.configureSockets() {
    val messageRepo by inject<MessageRepo>()
    val server by inject<CommonServer>()
    val scope by inject<CoroutineScope>()
    install(WebSockets) {
        pingPeriod = Duration.ofSeconds(15)
        timeout = Duration.ofSeconds(15)
        maxFrameSize = Long.MAX_VALUE
        masking = false
    }
    routing {
        webSocket("/ws") {
            server.setSocketCallback { string ->
                scope.launch {
                    send(string)
                }
            }
            LogHelper.log("Sockets", call.request.local.remoteHost)
            // websocketSession
            for (frame in incoming) {
                if (frame is Frame.Text) {
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
            }
        }
    }
}
