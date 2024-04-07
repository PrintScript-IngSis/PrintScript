plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "printscript"
include("parser")
include("common")
include("lexer")
include("interpreter")
include("formatter")
include("linter")
include("linter")
include("cli")
