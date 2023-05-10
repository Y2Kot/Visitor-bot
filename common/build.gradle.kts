plugins {
    id("java")
    kotlin("jvm")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation(libs.coroutines.core)
    implementation(libs.caffeince.cache)
}

kotlin {
    jvmToolchain(11)
}
