package dev.nordix.wsserver.server

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MessageRepo {

    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages = _messages.asStateFlow()

    fun receive(message: Message) {
        _messages.update { it.toMutableList().apply { add(message) } }
    }

    fun clear() {
        _messages.update { listOf() }
    }

}
