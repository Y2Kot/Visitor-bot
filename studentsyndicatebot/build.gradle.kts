plugins {
    id("java")
    kotlin("jvm")
    application
}

group = "ru.kudryavtsev"
version = "1.4"

application {
    mainClass.set("$group.BotAppKt")
}

dependencies {
    implementation(project(":photocutter:desktop"))
}
