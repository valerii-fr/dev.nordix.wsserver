package dev.nordix.wsserver.server

import dev.nordix.wsserver.di.appModule
import dev.nordix.wsserver.plugins.configureRouting
import dev.nordix.wsserver.plugins.configureSecurity
import dev.nordix.wsserver.plugins.configureSerialization
import dev.nordix.wsserver.plugins.configureSockets
import dev.nordix.wsserver.stateholder.KtorLifecycleObserver
import io.ktor.server.application.*
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin

fun Application.main() {
    install(Koin) {
        appModule()
    }
}

fun Application.module() {
    val observer by inject<KtorLifecycleObserver>()
    environment.monitor.subscribe(ApplicationStarting) { observer.onStarting() }
    environment.monitor.subscribe(ApplicationStarted) { observer.onStarted() }
    environment.monitor.subscribe(ApplicationStopping) { observer.onStopping() }
    environment.monitor.subscribe(ApplicationStopped) { observer.onStopped() }

    configureSerialization()
    configureSecurity()
    configureSockets()
    configureRouting()
}