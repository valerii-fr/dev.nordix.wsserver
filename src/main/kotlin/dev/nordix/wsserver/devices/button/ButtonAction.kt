package dev.nordix.wsserver.devices.button

import dev.nordix.wsserver.devices.DeviceAction

data class ButtonAction(
    override val author: String,
    override val code: String,
) : DeviceAction(author, code)
