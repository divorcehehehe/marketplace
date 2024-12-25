plugins {
    id("build-kmp")
}

kotlin {
    sourceSets {
        all { languageSettings.optIn("kotlin.RequiresOptIn") }

        commonMain {
            dependencies {
                implementation(kotlin("stdlib-common"))

                implementation(libs.cor)

                implementation(project(":common"))
                implementation(project(":stubs"))
            }
        }
        commonTest {
            dependencies {
                api(libs.coroutines.test)

                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        jvmMain {
            dependencies {
                implementation(kotlin("stdlib-jdk8"))
            }
        }
        jvmTest{
            dependencies {
                implementation(kotlin("test-junit"))
            }
        }
    }
}
