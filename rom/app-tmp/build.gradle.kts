plugins {
    id("build-jvm")
    application
}

application {
    mainClass.set("app.tmp.MainKt")
}

dependencies {
    implementation(project(":api-log"))
    implementation("marketplace.libs:lib-logging-common")
    implementation("marketplace.libs:lib-logging-logback")

    implementation(project(":common"))

    implementation(libs.coroutines.core)
}
