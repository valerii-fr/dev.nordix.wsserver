package dev.nordix.wsserver.server

import com.appstractive.dnssd.createNetService
import dev.nordix.wsserver.Constants.SERVICE_NAME
import dev.nordix.wsserver.Constants.SERVICE_PORT
import dev.nordix.wsserver.Constants.SERVICE_PROTOCOL
import dev.nordix.wsserver.Constants.SERVICE_VISIBLE_NAME
import dev.nordix.wsserver.helpers.KbHelper
import io.ktor.server.netty.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CommonServer (private val scope: CoroutineScope) {

    private var socketCallback : (String) -> Unit = { }
    private var _toggleState = MutableStateFlow(false)
    val toggleState = _toggleState.asStateFlow()

    private val service = createNetService(
        type = "$SERVICE_NAME.$SERVICE_PROTOCOL",
        name = SERVICE_VISIBLE_NAME,
        port = SERVICE_PORT,
    )

    fun setSocketCallback(block: (String) -> Unit) {
        socketCallback = block
    }

    fun toggle() {
        _toggleState.value = !_toggleState.value
        socketCallback.invoke(
            if (!_toggleState.value) "1.1" else "1.0"
        )
    }

    fun toggleToNoCallback(state: Boolean) {
        scope.launch {
            if (state) {
                KbHelper.startTimer()
            } else {
                KbHelper.pauseTimer()
            }
        }
        _toggleState.value = state
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