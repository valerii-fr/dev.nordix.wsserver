package dev.nordix.wsserver.devices

import java.io.Serializable

interface DeviceParameters : Serializable {
    val name: String
    val type: DeviceType
}
