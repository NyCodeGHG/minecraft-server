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
    implementation(kotlin("reflect"))
    implementation("org.jetbrains.kotlinx", "kotlinx-coroutines-core", "1.5.0-RC")
    implementation(platform("io.ktor:ktor-bom:1.5.4"))
    implementation("io.ktor", "ktor-io")
    implementation("io.ktor", "ktor-network")

    implementation("org.jetbrains.kotlinx", "kotlinx-serialization-core", "1.2.0")
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = "16"
        freeCompilerArgs = freeCompilerArgs + "-Xopt-in=kotlin.RequiresOptIn"
    }
}
