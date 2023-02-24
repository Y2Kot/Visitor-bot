package ru.kudryavtsev.domain.model

import java.io.File

sealed class AppContext(val volumePath: String) {
    val dbPath: String = "$volumePath$DB_NAME"

    object DevelopContext : AppContext("$projectDir$DEFAULT_PATH")
    object DockerContext : AppContext(DEFAULT_PATH)

    companion object {
        private const val DEFAULT_PATH = "/data/"
        private const val DOCKER_ENV = "/.dockerenv"
        private const val DB_NAME = "data.db"
        private val projectDir by lazy { System.getProperty("user.dir") }

        @JvmStatic
        private fun isDockerized(): Boolean {
            val environment = File(DOCKER_ENV)
            return environment.exists()
        }

        @JvmStatic
        fun getEnvironment(): AppContext = if (isDockerized()) DockerContext else DevelopContext
    }
}
