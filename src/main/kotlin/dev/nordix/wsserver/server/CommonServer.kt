package dev.nordix.wsserver.server

import com.appstractive.dnssd.createNetService
import dev.nordix.wsserver.Constants.SERVICE_NAME
import dev.nordix.wsserver.Constants.SERVICE_PORT
import dev.nordix.wsserver.Constants.SERVICE_PROTOCOL
import dev.nordix.wsserver.Constants.SERVICE_VISIBLE_NAME
import dev.nordix.wsserver.devices.DeviceAction
import dev.nordix.wsserver.devices.DeviceActions
import dev.nordix.wsserver.devices.DeviceType
import dev.nordix.wsserver.devices.DeviceEffect
import dev.nordix.wsserver.helpers.JsonHelper.json
import dev.nordix.wsserver.helpers.KbHelper
import dev.nordix.wsserver.server.model.ConnectedDevice
import io.ktor.server.netty.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.json.encodeToJsonElement

class CommonServer (private val scope: CoroutineScope) {

    private var socketCallbacks : MutableMap<String, (String) -> Unit> = mutableMapOf()

    private var _toggleState = MutableStateFlow(false)
    val toggleState = _toggleState.asStateFlow()

    private var _connectedHosts = MutableStateFlow((setOf<ConnectedDevice>()))
    val connectedHosts = _connectedHosts.asStateFlow()

    private val service = createNetService(
        type = "$SERVICE_NAME.$SERVICE_PROTOCOL",
        name = SERVICE_VISIBLE_NAME,
        port = SERVICE_PORT,
    )

    fun actByDevice(action: DeviceAction) {
        println("acting code: ${action.code} from author: ${action.author} in list of connected ${connectedHosts.value.map { it.host }}")
        connectedHosts.value.firstOrNull { it.host == action.author }
            ?.let { authoredDevice ->
                when(authoredDevice.type) {
                    DeviceType.Button -> {
                        when (action.code) {
                            DeviceActions.Click.actionName -> toggleAll()
                            DeviceActions.DoubleClick.actionName -> {
                                println("double click performed by $authoredDevice")
                            }
                            DeviceActions.LongClick.actionName -> {
                                println("long click performed by $authoredDevice")
                            }
                        }
                    }
                }
            }
    }

    fun setSocketCallback(host: String, block: (String) -> Unit) {
        socketCallbacks[host] = block
    }

    private fun postEffect(effect: DeviceEffect, vararg host: String) {
        host.forEach { deviceHost ->
            socketCallbacks[deviceHost]?.invoke(effect.code)
        }
    }

    fun connected(connectedDevice: ConnectedDevice) {
        println("updating $connectedDevice")
        _connectedHosts.update {
            it.toMutableSet().apply {
                removeIf { it.host == connectedDevice.host }
                add(connectedDevice)
            }
        }
    }

    fun disconnected(host: String) {
        _connectedHosts.update {
            it.toMutableSet().apply {
                removeIf { it.host == host }
            }
        }
        socketCallbacks.remove(host)
    }

    fun toggleAll() {
        _toggleState.value = !_toggleState.value
        scope.launch {
            if (toggleState.value) {
                KbHelper.startTimer()
                postEffect(DeviceEffect.LedOff, *socketCallbacks.keys.toTypedArray())
            } else {
                KbHelper.pauseTimer()
                postEffect(DeviceEffect.LedOn, *socketCallbacks.keys.toTypedArray())
            }
        }
    }

    fun unregister() {
        scope.launch {
            service.unregister()
        }
    }

    fun register() {
        scope.launch {
            service.register()
        }
    }

    operator fun invoke(args: Array<String>) {
        scope.launch {
            EngineMain.main(args)
        }
        register()
    }
}