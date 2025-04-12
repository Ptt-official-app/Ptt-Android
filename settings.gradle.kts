pluginManagement {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://plugins.gradle.org/m2/")
        maven(url = "https://cdn.reproio.com/android")
        maven(url = "https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}
rootProject.name = "Ptt"
include(":app")
include(":common")
include(":data")
include(":domain")
include(":shared")
