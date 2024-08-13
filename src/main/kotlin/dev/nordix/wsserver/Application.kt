package dev.nordix.wsserver

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import dev.nordix.wsserver.di.appModule
import dev.nordix.wsserver.server.CommonServer
import dev.nordix.wsserver.stateholder.KtorLifecycleObserver
import dev.nordix.wsserver.stateholder.KtorLifecycleObserver.ObserverCallback.OnStartedCallback
import dev.nordix.wsserver.stateholder.KtorLifecycleObserver.ObserverCallback.OnStoppedCallback
import dev.nordix.wsserver.ui.app
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

        Window(
            onCloseRequest = { exitApplication() },
            title = Constants.APP_TITLE,
            undecorated = true,
            resizable = false,
            content = {
                app(
                    onClose = { exitApplication() }
                )
            }
        )
    }
}
