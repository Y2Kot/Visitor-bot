package ru.kudryavtsev.domain.model

import java.io.File
import java.util.*

sealed class AppContext(val volumePath: String) {
    private val secretProperties = Properties().apply {
        load(File("$volumePath/$FILE_NAME").inputStream())
    }

    val dbPath: String = "$volumePath$DB_NAME"
    val token: String = secretProperties[TOKEN_KEY] as String

    object DevelopContext : AppContext("$projectDir$DEFAULT_PATH")
    object DockerContext : AppContext(DEFAULT_PATH)

    companion object {
        private const val DEFAULT_PATH = "/data/"
        private const val DOCKER_ENV = "/.dockerenv"
        private const val DB_NAME = "data.db"
        private const val FILE_NAME = "secret.properties"
        private const val TOKEN_KEY = "token"

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
