package dev.nordix.wsserver.di

import dev.nordix.wsserver.stateholder.KtorLifecycleObserver
import org.koin.dsl.module

val lifecycleModule = module {
    single { KtorLifecycleObserver(get()) }
}