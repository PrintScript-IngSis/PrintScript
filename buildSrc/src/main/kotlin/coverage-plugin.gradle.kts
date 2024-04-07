plugins {
    id("org.jetbrains.kotlinx.kover")
}

repositories {
    mavenCentral()
}

dependencies {
    tests()
}

koverReport {
    filters {
        excludes {
            //exclude main classes
            classes("org.example.MainKt")
        }
    }
    verify {
        rule {
            isEnabled = true
            bound {
                minValue = 80
            }
        }
    }
}
