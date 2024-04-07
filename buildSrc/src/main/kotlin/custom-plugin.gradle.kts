plugins {
    id("org.jetbrains.kotlin.jvm")
    id("org.jetbrains.kotlin.plugin.serialization")
    id("org.jlleitschuh.gradle.ktlint")
}

repositories {
    mavenCentral()
}

dependencies {
    kotlinxSerialization()
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}


