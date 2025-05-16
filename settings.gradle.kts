pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
    plugins {
        id("com.android.library") version "8.3.0" // Or whatever version you're using
        id("org.jetbrains.kotlin.android") version "1.9.10"
    }
}
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "Sinister"