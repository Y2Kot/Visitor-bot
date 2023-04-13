plugins {
    id("application")
    id("kotlin")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_11
    targetCompatibility = JavaVersion.VERSION_1_11
}

dependencies {
    api(project(":photocutter"))
    implementation(libs.bundles.cutter)
}