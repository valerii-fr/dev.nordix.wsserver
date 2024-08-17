package dev.nordix.wsserver

import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.window.*
import dev.nordix.wsserver.di.appModule
import dev.nordix.wsserver.server.CommonServer
import dev.nordix.wsserver.stateholder.KtorLifecycleObserver
import dev.nordix.wsserver.stateholder.KtorLifecycleObserver.ObserverCallback.OnStartedCallback
import dev.nordix.wsserver.stateholder.KtorLifecycleObserver.ObserverCallback.OnStoppedCallback
import dev.nordix.wsserver.ui.DefaultWidth
import dev.nordix.wsserver.ui.NordixApp
import dev.nordix.wsserver.ui.TopBarHeight
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import me.sample.library.resources.Res
import me.sample.library.resources.app_name
import org.jetbrains.compose.resources.stringResource
import org.koin.core.context.startKoin
import org.koin.java.KoinJavaComponent.inject

fun main(args: Array<String>) {

    startKoin { modules(appModule()) }

    val scope by inject<CoroutineScope>(CoroutineScope::class.java)
    val server by inject<CommonServer>(CommonServer::class.java)
    val observer by inject<KtorLifecycleObserver>(KtorLifecycleObserver::class.java)
    observer.setCallback(OnStartedCallback { server.register() })
    observer.setCallback(OnStoppedCallback { server.unregister() })



    if (args.none { it == "nogui" }) {
        scope.launch {
            server.invoke(args)
        }

        application {
            val state = rememberWindowState(
                size = DpSize(DefaultWidth, TopBarHeight),
                placement = WindowPlacement.Floating,
                position = WindowPosition.Aligned(Alignment.Center)
            )
            val trayState = rememberTrayState()

            if(state.isMinimized) {
                Tray(
                    state = trayState,
                    icon = painterResource("n.png"),
                    onAction = { state.isMinimized = false },
                    menu = {
                        Item(
                            "Exit",
                            onClick = { exitApplication() }
                        )
                    }
                )
            } else {
                Window(
                    onCloseRequest = { exitApplication() },
                    title = stringResource(Res.string.app_name),
                    undecorated = true,
                    state = state,
                    transparent = true,
                    content = {
                        NordixApp(
                            onClose = { exitApplication() },
                            windowState = state,
                        )
                    }
                )
            }

        }
    } else {
        server.invoke(args)
    }

}
