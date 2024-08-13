package dev.nordix.wsserver.helpers

object LogHelper {
    fun log(tag: String, message: String) {
        println(tag.take(30).padEnd(30, ' ') + " " + message)
    }
}