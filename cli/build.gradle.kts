plugins {
    id("custom-plugin")
}

group = "org.example"
version = "1.0-SNAPSHOT"

dependencies {
    implementation("com.github.ajalt:clikt:2.8.0")
    common()
    lexer()
    parser()
    interpreter()
    formatter()
    linter()
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "com.example.cli.CodeRunner"
    }
}
