plugins {
    kotlin("jvm") version "1.4.10"
    kotlin("plugin.serialization") version "1.4.10"
}

group = "de.nycode"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://jitpack.io")
}

val coroutines_version: String by project
val ktor_version: String by project
val logback_version: String by project
val guava_version: String by project
val serialization_version: String by project

dependencies {
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", coroutines_version)
    implementation("org.jetbrains.kotlinx", "kotlinx-serialization-json", serialization_version)
    implementation("io.ktor", "ktor-client-core", ktor_version)
    implementation("io.ktor", "ktor-client-apache", ktor_version)
    implementation("io.ktor", "ktor-client-json", ktor_version)
    implementation("io.ktor", "ktor-client-serialization", ktor_version)
    implementation("ch.qos.logback", "logback-classic", logback_version)
    implementation("com.google.guava", "guava", guava_version)
    implementation(kotlin("reflect"))
}
