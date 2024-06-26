plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.22")
    implementation("org.jetbrains.kotlin:kotlin-serialization:1.9.22")
    implementation("org.jlleitschuh.gradle:ktlint-gradle:12.1.0")
    implementation("org.jetbrains.kotlinx:kover-gradle-plugin:0.7.6")
}