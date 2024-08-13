package dev.nordix.wsserver.stateholder

import dev.nordix.wsserver.server.KtorStatus
import dev.nordix.wsserver.stateholder.KtorLifecycleObserver.ObserverCallback.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class KtorLifecycleObserver(
    private val scope: CoroutineScope
){
    private var onStartingCallback: OnStartingCallback? = null
    private var onStartedCallback: OnStartedCallback? = null
    private var onStoppedCallback: OnStoppedCallback? = null
    private var onStoppingCallback: OnStoppingCallback? = null

    private val _currentStatus: MutableStateFlow<KtorStatus> = MutableStateFlow(KtorStatus.Starting)
    val currentStatus: StateFlow<KtorStatus> = _currentStatus.asStateFlow()

    fun onStarting() = scope.launch {
        println("onStarting triggered")
        _currentStatus.value = KtorStatus.Starting
        onStartingCallback?.invoke()
    }
    fun onStarted() = scope.launch {
        _currentStatus.value = KtorStatus.Started
        println("onStarted triggered")
        onStartedCallback?.invoke()
    }
    fun onStopping() = scope.launch {
        _currentStatus.value = KtorStatus.Stopping
        println("onStopping triggered")
        onStoppingCallback?.invoke()
    }
    fun onStopped() = scope.launch {
        _currentStatus.value = KtorStatus.Stopped
        println("onStopped triggered")
        onStoppedCallback?.invoke()
    }

    fun setCallback(callback: ObserverCallback) : Boolean {
        when (callback) {
            is OnStartedCallback -> {
                if (onStartedCallback != null) return false
                onStartedCallback = callback
                println("onStarted set")
            }
            is OnStartingCallback -> {
                if (onStartingCallback != null) return false
                onStartingCallback = callback
                println("onStartingCallback set")
            }
            is OnStoppedCallback -> {
                if (onStoppedCallback != null) return false
                onStoppedCallback = callback
                println("onStoppedCallback set")
            }
            is OnStoppingCallback -> {
                if (onStoppingCallback != null) return false
                onStoppingCallback = callback
                println("onStoppingCallback set")
            }
        }
        return true
    }

    sealed class ObserverCallback(open val block: () -> Unit) : () -> Unit {
        override fun invoke() = block()

        class OnStartedCallback(override val block: () -> Unit) : ObserverCallback(block)
        class OnStartingCallback(override val block: () -> Unit) : ObserverCallback(block)
        class OnStoppedCallback(override val block: () -> Unit) : ObserverCallback(block)
        class OnStoppingCallback(override val block: () -> Unit) : ObserverCallback(block)
    }

}
