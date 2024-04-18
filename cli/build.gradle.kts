plugins {
    id("custom-plugin")
    id("publish-plugin")
    application
}

group = "org.example"
version = "1.1"

dependencies {
    implementation("com.github.ajalt.clikt:clikt:4.3.0")
    common()
    lexer()
    parser()
    interpreter()
    formatter()
    linter()
}

application {
    mainClass = "CodeRunnerKt"
}

tasks.named<JavaExec>("run") {
    standardInput = System.`in`
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.example.cli.CodeRunner"
    }
}
