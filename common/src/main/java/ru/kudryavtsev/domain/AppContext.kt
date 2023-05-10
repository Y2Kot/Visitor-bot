package ru.kudryavtsev.domain

import java.io.File
import java.util.*

sealed class AppContext(
    val tokenName: String,
    val volumePath: String
) {
    private val secretProperties = Properties().apply {
        load(File("$volumePath/$FILE_NAME").inputStream())
    }

    val dbPath: String = "$volumePath$DB_NAME"
    val token: String = secretProperties[tokenName] as String

    private class DevelopContext(tokenName: String) : AppContext(
        tokenName = tokenName,
        volumePath = "$projectDir$DEFAULT_PATH"
    )

    private class DockerContext(tokenName: String) : AppContext(
        tokenName = tokenName,
        volumePath = DEFAULT_PATH
    )

    companion object {
        private const val DEFAULT_PATH = "/data/"
        private const val DOCKER_ENV = "/.dockerenv"
        private const val DB_NAME = "data.db"
        private const val FILE_NAME = "secret.properties"

        private val projectDir by lazy { System.getProperty("user.dir") }

        @JvmStatic
        private fun isDockerized(): Boolean {
            val environment = File(DOCKER_ENV)
            return environment.exists()
        }

        @JvmStatic
        fun getEnvironment(tokenName: String): AppContext =
            if (isDockerized()) DockerContext(tokenName) else DevelopContext(tokenName)
    }
}
