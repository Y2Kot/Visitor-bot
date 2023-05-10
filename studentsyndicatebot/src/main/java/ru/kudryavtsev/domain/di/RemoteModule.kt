package ru.kudryavtsev.domain.di

import org.koin.dsl.module
import ru.kudryavtsev.datasource.remote.BotApi
import ru.kudryavtsev.datasource.remote.BotService
import ru.kudryavtsev.datasource.remote.IBotService

val remoteModule = module {
    single {
        BotApi(get())
    }
    factory<IBotService> { BotService(get()) }
}
