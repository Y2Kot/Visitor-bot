package ru.kudryavtsev.domain.di

import org.koin.dsl.module
import ru.kudryavtsev.domain.repository.AdministratorRepository
import ru.kudryavtsev.domain.repository.BotRepository
import ru.kudryavtsev.domain.repository.StudentsRepository
import ru.kudryavtsev.domain.repository.VisitRepository

val domainModule = module {
    factory { BotRepository(get()) }
    factory { StudentsRepository() }
    factory { VisitRepository() }
    factory { AdministratorRepository() }
}
