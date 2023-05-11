plugins {
    id("java")
    kotlin("jvm")
}

group = "ru.kudryavtsev"
version = "1.4"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(11)
}
