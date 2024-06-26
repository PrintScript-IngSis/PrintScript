import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.kotlin.dsl.project

object Dependencies {
    const val tests = "org.jetbrains.kotlin:kotlin-test"
    const val jsonSerialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3"
    const val gson = "com.google.code.gson:gson:2.10.1"
}

fun DependencyHandler.tests() {
    testImplementation(Dependencies.tests)
}

fun DependencyHandler.kotlinxSerialization() {
    implementation(Dependencies.jsonSerialization)
}

fun DependencyHandler.gson() {
    implementation(Dependencies.gson)
}

fun DependencyHandler.common() {
    implementation(project(":common"))
}
fun DependencyHandler.lexer() {
    implementation(project(":lexer"))
}
fun DependencyHandler.parser() {
    implementation(project(":parser"))
}
fun DependencyHandler.interpreter() {
    implementation(project(":interpreter"))
}

fun DependencyHandler.formatter() {
    implementation(project(":formatter"))
}

fun DependencyHandler.linter() {
    implementation(project(":linter"))
}