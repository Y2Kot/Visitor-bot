package ru.kudryavtsev.domain.di

import ru.kudryavtsev.datasource.local.services.AdministratorDaoService
import ru.kudryavtsev.datasource.local.services.StudentDaoService
import ru.kudryavtsev.datasource.local.services.VisitDaoService
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.kudryavtsev.domain.repository.AdministratorRepository
import ru.kudryavtsev.domain.repository.BotRepository
import ru.kudryavtsev.domain.repository.StudentsRepository
import ru.kudryavtsev.domain.repository.VisitRepository
import ru.kudryavtsev.domain.service.StudentStateService
import ru.kudryavtsev.domain.usecase.GetOrInitStudentStateUseCase
import ru.kudryavtsev.domain.usecase.ReceiveMessagesUseCase
import ru.kudryavtsev.domain.usecase.SendMessageUseCase

val domainModule = module {
    factoryOf(::AdministratorDaoService)
    factoryOf(::StudentDaoService)
    factoryOf(::VisitDaoService)

    factoryOf(::BotRepository)
    factoryOf(::StudentsRepository)
    factoryOf(::VisitRepository)
    factoryOf(::AdministratorRepository)

    singleOf(::StudentStateService)

    factoryOf(::GetOrInitStudentStateUseCase)
    factoryOf(::ReceiveMessagesUseCase)
    factoryOf(::SendMessageUseCase)
}
