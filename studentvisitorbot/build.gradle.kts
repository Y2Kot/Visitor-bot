import com.bmuschko.gradle.docker.tasks.image.DockerBuildImage
import com.bmuschko.gradle.docker.tasks.image.DockerPushImage
import com.bmuschko.gradle.docker.tasks.image.Dockerfile
import java.util.Properties

plugins {
    id("java")
    kotlin("jvm")
    id("com.github.johnrengelman.shadow")
    id("com.bmuschko.docker-remote-api")
    application
}

val localProperties: Properties by rootProject.extra

group = "ru.kudryavtsev"
version = "1.5"

application {
    mainClass.set("$group.BotAppKt")
}

val copyJarToDocker by tasks.creating(Copy::class) {
    dependsOn(tasks.named("shadowJar"))
    from("$buildDir/libs/${project.name}-$version-all.jar")
    into("$buildDir/docker/")
}

val createDockerFile by tasks.creating(Dockerfile::class) {
    from(ext["docker.image"] as String)
    label(mapOf("maintainer" to ext["docker.maintainer"] as String))
    workingDir("VisitorBot")
    copyFile("${project.name}-$version-all.jar", "${project.name}.jar")
    volume("data")
    defaultCommand("java", "-jar", "${project.name}.jar")
}

val buildImage by tasks.creating(DockerBuildImage::class) {
    dependsOn(createDockerFile, copyJarToDocker)
    inputDir.set(createDockerFile.destDir)
    images.add("${localProperties["dockerhub.name"]}/${project.name}:$version")
}

val publishImage by tasks.creating(DockerPushImage::class) {
    dependsOn(buildImage)
    images.set(buildImage.images)
}

dependencies {
    implementation(projects.common)
    implementation(projects.csvLoader)
    implementation(projects.googleSheets)

    implementation(libs.coroutines.core)
    implementation(libs.telegram.bot)
    implementation(libs.koin.core)
    implementation(libs.bundles.database)
    implementation(libs.bundles.logger)

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(11)
}
