plugins {
    id("application")
    id("kotlin")
}

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

dependencies {
    api(project(":photocutter"))
    implementation(libs.bundles.cutter)
}