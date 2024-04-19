plugins {
    id("custom-plugin")
    id("publish-plugin")
    id("coverage-plugin")
}

version = "1.1.0-SNAPSHOT"

dependencies {
    common()
    gson()
}
