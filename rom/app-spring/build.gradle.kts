plugins {
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.spring.dependencies)
    alias(libs.plugins.spring.kotlin)
    alias(libs.plugins.kotlinx.serialization)
    id("build-jvm")
}

dependencies {
    implementation(libs.spring.actuator)
    implementation(libs.spring.webflux)
    implementation(libs.spring.webflux.ui)
    implementation(libs.jackson.kotlin)
    implementation(kotlin("reflect"))
    implementation(kotlin("stdlib"))

    implementation(libs.coroutines.core)
    implementation(libs.coroutines.reactor)
    implementation(libs.coroutines.reactive)
    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)

    // Внутренние модели
    implementation(project(":common"))
    implementation(project(":app-common"))
    implementation("marketplace.libs:lib-logging-logback")

    // v1 api
    implementation(project(":api-v1-jackson"))
    implementation(project(":api-v1-mappers"))

    // v2 api
    implementation(project(":api-v2-kmp"))
    implementation(project(":api-v2-mappers"))

    // biz
    implementation(project(":biz"))

    // DB
    implementation(project(":repo-stubs"))
    implementation(project(":repo-inmemory"))
    testImplementation(project(":repo-common"))
    testImplementation(project(":stubs"))

    // tests
    testImplementation(kotlin("test-junit5"))
    testImplementation(libs.spring.test)
    testImplementation(libs.mockito.kotlin)

    testImplementation(libs.spring.mockk)
}

tasks {
    withType<ProcessResources> {
        val files = listOf("spec-v1", "spec-v2").map {
            rootProject.ext[it]
        }
        from(files) {
            into("/static")
            filter {
                // Устанавливаем версию в сваггере
                it.replace("\${VERSION_APP}", project.version.toString())
            }

        }
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
