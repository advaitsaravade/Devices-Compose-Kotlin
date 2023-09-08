buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        val kotlinVersion = "1.8.10"
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.47")
        classpath(kotlin("serialization", version = kotlinVersion))
    }
}

plugins {
    id("com.android.application") version "8.1.1" apply false
    id("org.jetbrains.kotlin.android") version "1.8.10" apply false
}