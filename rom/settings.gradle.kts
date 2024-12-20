rootProject.name = "rom"

dependencyResolutionManagement {
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

pluginManagement {
    includeBuild("../build-plugin")
    plugins {
        id("build-jvm") apply false
        id("build-kmp") apply false
    }
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

include(":api-v1-jackson")
include(":api-v1-mappers")
include(":api-v2-kmp")
include(":api-v2-mappers")

include(":api-log")

include(":common")
include(":biz")
include(":stubs")

include(":app-common")
include(":app-tmp")
include(":app-spring")
