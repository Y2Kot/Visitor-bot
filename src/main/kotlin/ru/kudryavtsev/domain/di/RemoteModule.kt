package ru.kudryavtsev.domain.di

import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.kudryavtsev.datasource.remote.BotApi
import ru.kudryavtsev.datasource.remote.BotService

val remoteModule = module {
    singleOf(::BotApi)
    factoryOf(::BotService)
}
