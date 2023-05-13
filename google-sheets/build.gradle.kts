plugins {
    id("java")
    kotlin("jvm")
}

group = "ru.kudryavtsev"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(projects.common)

    implementation(kotlin("stdlib-jdk8"))
    implementation("com.google.api-client:google-api-client:2.0.0")
    implementation("com.google.oauth-client:google-oauth-client-jetty:1.34.1")
    implementation("com.google.apis:google-api-services-sheets:v4-rev20220927-2.0.0")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(11)
}
