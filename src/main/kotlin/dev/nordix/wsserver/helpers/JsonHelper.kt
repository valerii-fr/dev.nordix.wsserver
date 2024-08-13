package dev.nordix.wsserver.helpers

import kotlinx.serialization.json.*

object JsonHelper {
    val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
        prettyPrint = true
        encodeDefaults = true
        classDiscriminator = "#class"
    }
}
