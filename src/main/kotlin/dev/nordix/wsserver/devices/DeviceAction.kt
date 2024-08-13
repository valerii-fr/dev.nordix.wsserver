package dev.nordix.wsserver.devices

import kotlinx.serialization.Serializable

@Serializable
open class DeviceAction(
    open val author: String,
    open val code: String
)
