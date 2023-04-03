plugins {
    id("application")
    id("kotlin")
}

repositories {
    maven("https://maven.nexus.qoollo.com/")
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

dependencies {
    api(project(":photocutter"))

    implementation("com.microsoft.onnxruntime:onnxruntime:latest.release")
    implementation("org.bytedeco:javacv-platform:1.5.8")
}