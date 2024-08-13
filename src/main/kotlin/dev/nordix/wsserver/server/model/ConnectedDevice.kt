package dev.nordix.wsserver.server.model

import dev.nordix.wsserver.devices.DeviceType
import java.time.Instant

data class ConnectedDevice(
    val type: DeviceType,
    val name: String,
    val host: String,
    val connectedAt: Instant,
)
