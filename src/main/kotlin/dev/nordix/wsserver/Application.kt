package dev.nordix.wsserver

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dev.nordix.wsserver.di.appModule
import dev.nordix.wsserver.server.CommonServer
import dev.nordix.wsserver.stateholder.KtorLifecycleObserver
import dev.nordix.wsserver.stateholder.KtorLifecycleObserver.ObserverCallback.OnStartedCallback
import dev.nordix.wsserver.stateholder.KtorLifecycleObserver.ObserverCallback.OnStoppedCallback
import dev.nordix.wsserver.ui.app
import me.sample.library.resources.Res
import me.sample.library.resources.app_name
import org.jetbrains.compose.resources.stringResource
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.inject

fun main(args: Array<String>) {

    startKoin { modules(appModule()) }

    val server by inject<CommonServer>(CommonServer::class.java)
    val observer by inject<KtorLifecycleObserver>(KtorLifecycleObserver::class.java)
    observer.setCallback(OnStartedCallback { server.register() } )
    observer.setCallback(OnStoppedCallback { server.unregister() } )
    server.invoke(args)

    application {
        val state = rememberWindowState()

        Window(
            onCloseRequest = { exitApplication() },
            title = stringResource(Res.string.app_name),
            undecorated = true,
            state = state,
            transparent = true,
            content = {
                app(
                    onClose = { exitApplication() },
                    windowState = state,
                )
            }
        )
    }
}
