package dev.nordix.wsserver.devices.button

import dev.nordix.wsserver.devices.DeviceParameters
import dev.nordix.wsserver.devices.DeviceType
import kotlinx.serialization.Serializable

@Serializable
data class ButtonParameters(
    override val name: String,
    override val type: DeviceType = DeviceType.Button,
) : DeviceParameters
