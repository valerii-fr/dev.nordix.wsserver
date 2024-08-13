package dev.nordix.wsserver.di

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.dsl.module

fun appModule() = listOf(httpModule, scope, lifecycleModule)

val scope = module {
    single {
        CoroutineScope(Dispatchers.IO)
    }
}
