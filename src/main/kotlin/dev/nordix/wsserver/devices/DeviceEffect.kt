package dev.nordix.wsserver.devices

/**
 * led states is inverted because of my laziness
 */
enum class DeviceEffect(val code: String) {
    LedOn("led_on"),
    LedOff("led_off"),
    StartTimer("start_timer"),
    StopTimer("stop_timer"),
}
