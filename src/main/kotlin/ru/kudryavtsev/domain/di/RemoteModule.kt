package ru.kudryavtsev.domain.di

import org.koin.dsl.module
import ru.kudryavtsev.datasource.remote.BotApi
import ru.kudryavtsev.datasource.remote.BotService

val remoteModule = module {
    single { BotApi() }
    factory { BotService(get()) }
}