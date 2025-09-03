pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "MoneyApp"

include(":androidApp")
include(":desktopApp")
include(":shared")
