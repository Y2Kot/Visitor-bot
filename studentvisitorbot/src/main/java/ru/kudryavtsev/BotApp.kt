package ru.kudryavtsev

import com.qoollo.common.model.FilePolicy
import com.qoollo.logback.LogbackLogger
import com.qoollo.logger.QoolloLogger
import com.qoollo.logger.logi
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
import org.koin.core.logger.Level
import org.koin.core.logger.PrintLogger
import ru.kudryavtsev.datasource.local.entity.Administrators
import ru.kudryavtsev.datasource.local.entity.Students
import ru.kudryavtsev.datasource.local.entity.Visits
import ru.kudryavtsev.domain.AppContext
import ru.kudryavtsev.domain.BotProcessor
import ru.kudryavtsev.domain.di.domainModule
import ru.kudryavtsev.domain.di.remoteModule
import java.sql.Connection

suspend fun main() {
    val koin = startKoin {
        logger(PrintLogger(Level.INFO))
        modules(domainModule, remoteModule)
    }

    val scope = CoroutineScope(Dispatchers.Default)
    val currentContext = koin.koin.get<AppContext>()

    initializeLogger(currentContext)

    logi(tag = "BotApp") {
        "selected context: ${currentContext.javaClass.simpleName}\n" +
                "volume path: ${currentContext.volumePath}\n" +
                "db path: ${currentContext.dbPath}"
    }

    initializeDb(currentContext)

    val botProcessor = koin.koin.get<BotProcessor>()
    botProcessor.messages.launchIn(scope)

    awaitCancellation()
}

private fun initializeDb(context: AppContext) {
    Database.connect("jdbc:sqlite:${context.dbPath}", "org.sqlite.JDBC")
    TransactionManager.manager.defaultIsolationLevel = Connection.TRANSACTION_SERIALIZABLE
    transaction {
        addLogger(StdOutSqlLogger)
        SchemaUtils.create(Visits, Students, Administrators)
    }
}

private fun initializeLogger(context: AppContext) {
    QoolloLogger {
        val logbackKey = "logback"
        val filePolicy = FilePolicy(
            fileName = "VisitorBotLoggerFiles",
            filesPath = context.volumePath,
            fileSize = 1024 * 1024 * 6,
            filesCount = 5,
            append = true
        )
        registerLogger(logbackKey, LogbackLogger(logbackKey))
        enableConsoleLogging(logbackKey)
        enableFileLogging(logbackKey, filePolicy)
    }
}
