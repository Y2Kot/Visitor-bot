package ru.kudryavtsev

import com.qoollo.logback.LogbackLogger
import com.qoollo.logger.QoolloLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.launchIn
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.TransactionManager
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.core.context.startKoin
import ru.kudryavtsev.domain.BotController
import ru.kudryavtsev.domain.di.domainModule
import ru.kudryavtsev.domain.di.remoteModule
import java.sql.Connection

suspend fun main() {
    initializeDb()
    initializeLogger()
    startKoin {
        modules(domainModule, remoteModule)
    }
    val scope = CoroutineScope(Dispatchers.Default)
    val botController = BotController()
    botController.messages.launchIn(scope)
    awaitCancellation()
}

private fun initializeDb() {
    Database.connect("jdbc:sqlite:/data/data.db", "org.sqlite.JDBC")
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
    transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(ru.kudryavtsev.datasource.local.entity.Visits)
        SchemaUtils.create(ru.kudryavtsev.datasource.local.entity.Students)
    }
}

private fun initializeLogger() {
    QoolloLogger {
        val logbackKey = "logback"
        registerLogger(logbackKey, LogbackLogger(logbackKey))
        enableConsoleLogging(logbackKey)
    }
}