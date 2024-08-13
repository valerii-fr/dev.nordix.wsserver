rootProject.name = "dev.nordix.wsserver"

pluginManagement {
    repositories {
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        google()
        gradlePluginPortal()
        mavenCentral()
    }

    plugins {
        kotlin("jvm").version(extra["kotlin_version"] as String)
        kotlin("plugin.compose").version(extra["kotlin_version"] as String)
        id("org.jetbrains.compose").version(extra["compose.version"] as String)
        id("org.jetbrains.kotlin.plugin.serialization").version(extra["kotlin_version"] as String)
        id("io.ktor.plugin").version(extra["ktor_version"] as String)
        id("com.google.devtools.ksp").version(extra["ksp_version"] as String)
        id("application")
    }
}
