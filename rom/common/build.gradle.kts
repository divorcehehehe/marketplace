plugins {
    id("build-kmp")
}

group = rootProject.group
version = rootProject.version

kotlin {
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))

                api(libs.kotlinx.datetime)
                implementation(libs.coroutines.core)
                api("marketplace.libs:lib-logging-common")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
    }
}
