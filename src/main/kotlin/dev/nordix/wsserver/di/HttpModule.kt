package dev.nordix.wsserver.di

import dev.nordix.wsserver.server.CommonServer
import dev.nordix.wsserver.server.MessageRepo
import org.koin.dsl.module

val httpModule = module {
    single { CommonServer(get()) }
    single { MessageRepo() }
}