plugins {
    id("custom-plugin")
    id("publish-plugin")
    id("coverage-plugin")
}

version = "1.1.5"

dependencies {
    common()
    gson()
}
