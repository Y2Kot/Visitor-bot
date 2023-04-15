plugins {
    id("java")
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(libs.coroutines.core)
}

kotlin {
    jvmToolchain(11)
}