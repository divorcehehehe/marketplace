plugins {
    id("build-kmp")
}

kotlin {
    sourceSets {
        val coroutinesVersion: String by project
        commonMain {
            dependencies {
                implementation(libs.coroutines.core)
                implementation(kotlin("stdlib-jdk8"))

                // transport models
                implementation(project(":common"))
                implementation(project(":api-log"))

                implementation(project(":biz"))
            }
        }
        commonTest {
            dependencies {
                implementation(libs.coroutines.test)

                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))

                implementation(project(":api-v2-kmp"))
                implementation(project(":api-v2-mappers"))
            }
        }

        jvmTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        nativeTest {
            dependencies {
                implementation(kotlin("test"))
            }
        }
    }
}
