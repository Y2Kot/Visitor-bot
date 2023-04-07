import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.DockerPushImage
import com.bmuschko.gradle.docker.tasks.image.Dockerfile
import java.util.Properties

plugins {
    kotlin("jvm") version "1.8.0"
    id("com.bmuschko.docker-remote-api") version "9.2.1"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    application
}

val localProperties = Properties().apply {
    load(project.rootProject.file("local.properties").inputStream())
}

group = "ru.kudryavtsev"
version = "1.4"

repositories {
    maven(url = "https://maven.nexus.qoollo.com/")
}

subprojects {
    repositories {
        maven(url = "https://maven.nexus.qoollo.com/")
    }
}
application {
    mainClass.set("$group.BotAppKt")
}

docker {
    registryCredentials {
        username.set(localProperties["dockerhub.name"] as String)
        password.set(localProperties["dockerhub.password"] as String)
    }
}

dependencies {
    implementation(libs.coroutines.core)
    implementation(libs.telegram.bot)
    implementation(libs.koin.core)
    implementation(libs.bundles.database)
    implementation(libs.caffeince.cache)
    implementation(libs.bundles.logger)

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

// region Useful for debug

//gradle.taskGraph.beforeTask {
//    println("executing $this")
//}
//
//gradle.taskGraph.afterTask {
//    if (state.failure != null) {
//        println("$this failed")
//    } else {
//        println("$this done")
//    }
//}

// endregion

val copyJarToDocker by tasks.creating(Copy::class) {
    dependsOn(tasks.named("shadowJar"))
    from("$buildDir/libs/${rootProject.name}-$version-all.jar")
    into("$buildDir/docker/")
}

val createDockerFile by tasks.creating(Dockerfile::class) {
    from(ext["docker.image"] as String)
    label(mapOf("maintainer" to ext["docker.maintainer"] as String))
    workingDir("VisitorBot")
    copyFile("${rootProject.name}-$version-all.jar", "${rootProject.name}.jar")
    volume("data")
    defaultCommand("java", "-jar", "${rootProject.name}.jar")
}

val buildImage by tasks.creating(DockerBuildImage::class) {
    dependsOn(createDockerFile, copyJarToDocker)
    inputDir.set(createDockerFile.destDir)
    images.add("${localProperties["dockerhub.name"]}/${rootProject.name}:$version")
}

val publishImage by tasks.creating(DockerPushImage::class) {
    dependsOn(buildImage)
    images.set(buildImage.images)
}

kotlin {
    jvmToolchain(11)
}

