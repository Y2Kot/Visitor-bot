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
version = "1.3"

repositories {
    maven(url = "https://maven.nexus.qoollo.com/")
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
    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    // Telegram bot
    implementation("org.telegram:telegrambots:6.5.0")
    // Di
    val koinVersion = "3.3.3"
    implementation("io.insert-koin:koin-core:$koinVersion")
    // Database
    val exposedVersion = "0.41.1"
    implementation("org.jetbrains.exposed:exposed-core:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-dao:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-jdbc:$exposedVersion")
    implementation("org.jetbrains.exposed:exposed-java-time:$exposedVersion")
    implementation("org.xerial:sqlite-jdbc:3.40.1.0")
    // LRU
    implementation("com.github.ben-manes.caffeine:caffeine:3.1.3")
    // Logger
    val qoolloLogger = "1.0.0.29312"
    implementation("com.qoollo.qoollo-logger:qoollo-logger:$qoolloLogger")
    implementation("com.qoollo.qoollo-logger:logback-logger:$qoolloLogger")
    implementation("ch.qos.logback:logback-classic:1.4.5")

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

