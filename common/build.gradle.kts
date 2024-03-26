plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.9.22"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    tests()
    kotlinxSerialization()
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}