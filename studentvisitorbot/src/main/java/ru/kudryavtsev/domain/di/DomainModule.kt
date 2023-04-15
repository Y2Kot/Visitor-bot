@file:Suppress("RemoveExplicitTypeArguments")

package ru.kudryavtsev.domain.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import ru.kudryavtsev.datasource.local.services.AdministratorDaoService
import ru.kudryavtsev.datasource.local.services.StudentDaoService
import ru.kudryavtsev.datasource.local.services.VisitDaoService
import ru.kudryavtsev.domain.BotProcessor
import ru.kudryavtsev.domain.controller.HelpController
import ru.kudryavtsev.domain.controller.InfoController
import ru.kudryavtsev.domain.controller.RegisterController
import ru.kudryavtsev.domain.controller.UndefinedController
import ru.kudryavtsev.domain.controller.UploadImageController
import ru.kudryavtsev.domain.controller.VisitDisciplineController
import ru.kudryavtsev.AppContext
import ru.kudryavtsev.domain.repository.AdministratorRepository
import ru.kudryavtsev.domain.repository.BotRepository
import ru.kudryavtsev.domain.repository.StudentsRepository
import ru.kudryavtsev.domain.repository.VisitRepository
import ru.kudryavtsev.domain.service.StudentStateService
import ru.kudryavtsev.domain.usecase.CheckAdminPermissionUseCase
import ru.kudryavtsev.domain.usecase.GetAllStudentsUseCase
import ru.kudryavtsev.domain.usecase.GetOrInitStudentStateUseCase
import ru.kudryavtsev.domain.usecase.GetStudentUseCase
import ru.kudryavtsev.domain.usecase.ReceiveMessagesUseCase
import ru.kudryavtsev.domain.usecase.RegisterStudentUseCase
import ru.kudryavtsev.domain.usecase.RegisterVisitUseCase
import ru.kudryavtsev.domain.usecase.SendMessageUseCase
import ru.kudryavtsev.domain.usecase.UpdateStudentStateUseCase

val domainModule = module {
    single<AppContext> { AppContext.getEnvironment() }

    factoryOf(::AdministratorDaoService)
    factoryOf(::StudentDaoService)
    factoryOf(::VisitDaoService)

    factoryOf(::BotRepository)
    factoryOf(::StudentsRepository)
    factoryOf(::VisitRepository)
    factoryOf(::AdministratorRepository)

    single { StudentStateService() }

    factoryOf(::CheckAdminPermissionUseCase)
    factoryOf(::GetAllStudentsUseCase)
    factoryOf(::GetOrInitStudentStateUseCase)
    factoryOf(::GetStudentUseCase)
    factoryOf(::ReceiveMessagesUseCase)
    factoryOf(::RegisterStudentUseCase)
    factoryOf(::RegisterVisitUseCase)
    factoryOf(::SendMessageUseCase)
    factoryOf(::UpdateStudentStateUseCase)

    factoryOf(::HelpController)
    factoryOf(::InfoController)
    factoryOf(::RegisterController)
    factoryOf(::UndefinedController)
    factoryOf(::UploadImageController)
    factoryOf(::VisitDisciplineController)

    factoryOf(::BotProcessor)
}
