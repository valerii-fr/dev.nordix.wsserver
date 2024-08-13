package dev.nordix.wsserver.server.model

import java.time.Instant

data class Message(
    val from: String,
    val time: Instant,
    val text: String,
)
