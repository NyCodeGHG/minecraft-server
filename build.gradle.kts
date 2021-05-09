plugins {
    kotlin("jvm") version "1.5.0"
    kotlin("plugin.serialization") version "1.5.0"
}

group = "de.nycode"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.5.0-RC")
    implementation(platform("io.ktor:ktor-bom:1.5.4"))
    implementation("io.ktor", "ktor-io")
}
