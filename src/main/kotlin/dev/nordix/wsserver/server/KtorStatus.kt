package dev.nordix.wsserver.server

sealed interface  KtorStatus {
    data object Starting : KtorStatus
    data object Stopping : KtorStatus
    data object Started : KtorStatus
    data object Stopped : KtorStatus
}
