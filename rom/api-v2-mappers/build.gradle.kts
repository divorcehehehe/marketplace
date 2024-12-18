plugins {
    id("build-jvm")
}

group = rootProject.group
version = rootProject.version

dependencies {
    implementation(kotlin("stdlib"))
    implementation(project(":api-v2-kmp"))
    implementation(project(":common"))

    testImplementation(kotlin("test-junit"))
}