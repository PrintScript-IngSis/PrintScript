plugins {
    id("custom-plugin")
}

version = "1.0.0"

dependencies {
    common()
    lexer()
    parser()
    interpreter()
    formatter()
    linter()
}

tasks.register<Copy>("copyPreCommitGitHook") {
    from(".scripts/pre-commit") {
        fileMode = 493
    }
    into(".git/hooks")
}

tasks {
    build {
        dependsOn("copyPreCommitGitHook")
    }
}
