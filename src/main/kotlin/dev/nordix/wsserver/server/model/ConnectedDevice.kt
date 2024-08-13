package dev.nordix.wsserver.server.model

import java.time.Instant

data class ConnectedDevice(
    val name: String,
    val host: String,
    val connectedAt: Instant,
)
