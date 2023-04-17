package ru.kudryavtsev

import com.qoollo.common.model.FilePolicy
import com.qoollo.logback.LogbackLogger
import com.qoollo.logger.QoolloLogger
import com.qoollo.logger.logi
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.awaitCancellation
import kotlinx.coroutines.flow.launchIn
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import org.koin.core.logger.PrintLogger
import ru.kudryavtsev.domain.AppContext
import ru.kudryavtsev.domain.BotProcessor
import ru.kudryavtsev.domain.di.domainModule
import ru.kudryavtsev.domain.di.remoteModule

suspend fun main() {
    val koin = startKoin {
        logger(PrintLogger(Level.INFO))
        modules(domainModule, remoteModule)
    }

    val scope = koin.koin.get<CoroutineScope>()
    val currentContext = koin.koin.get<AppContext>()

    initializeLogger(currentContext)

    logi(tag = "BotApp") {
        "selected context: ${currentContext.javaClass.simpleName}\n" +
                "volume path: ${currentContext.volumePath}\n"
    }

    val botProcessor = koin.koin.get<BotProcessor>()
    botProcessor.messages.launchIn(scope)

    awaitCancellation()
}

private fun initializeLogger(context: AppContext) {
    QoolloLogger {
        val logbackKey = "logback"
        val filePolicy = FilePolicy(
            fileName = "SyndicateBotLoggerFiles",
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
