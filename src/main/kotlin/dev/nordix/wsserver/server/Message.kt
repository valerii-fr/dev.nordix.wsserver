package dev.nordix.wsserver.server

import java.time.Instant

data class Message(
    val from: String,
    val time: Instant,
    val text: String,
)
