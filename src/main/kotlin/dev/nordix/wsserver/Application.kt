package dev.nordix.wsserver

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import dev.nordix.wsserver.di.appModule
import dev.nordix.wsserver.ui.app
import me.sample.library.resources.Res
import me.sample.library.resources.app_name
import org.jetbrains.compose.resources.stringResource
import org.koin.core.context.startKoin

fun main(args: Array<String>) {

    startKoin { modules(appModule()) }
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
                    args = args,
                )
            }
        )
    }
}
