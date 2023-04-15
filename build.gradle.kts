import java.util.Properties

plugins {
    kotlin("jvm") version "1.8.20"
    id("com.bmuschko.docker-remote-api") version "9.2.1"
    id("com.github.johnrengelman.shadow") version "7.1.2"
    application
}

val localProperties: Properties by extra {
    Properties().apply {
        load(project.rootProject.file("local.properties").inputStream())
    }
}

repositories {
    maven("https://maven.nexus.qoollo.com/")
}

subprojects {
    repositories {
        maven("https://maven.nexus.qoollo.com/")
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
