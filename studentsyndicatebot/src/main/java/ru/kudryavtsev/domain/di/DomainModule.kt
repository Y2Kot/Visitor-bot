package ru.kudryavtsev.domain.di

import com.example.photocutter.desktop.DesktopFaceDetectionService
import com.example.photocutter.desktop.FaceFramesDrawer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import ru.kudryavtsev.domain.AppContext
import ru.kudryavtsev.domain.BotProcessor
import ru.kudryavtsev.domain.FaceCountService
import ru.kudryavtsev.domain.controller.FaceCountController
import ru.kudryavtsev.domain.controller.HelpController
import ru.kudryavtsev.domain.controller.RegisterController
import ru.kudryavtsev.domain.controller.UndefinedController
import ru.kudryavtsev.domain.repository.BotRepository
import ru.kudryavtsev.domain.service.UserStateService
import ru.kudryavtsev.domain.usecase.GetOrInitUserStateUseCase
import ru.kudryavtsev.domain.usecase.ReceiveMessagesUseCase
import ru.kudryavtsev.domain.usecase.SendMessageUseCase
import ru.kudryavtsev.domain.usecase.UpdateUserStateUseCase

private const val TOKEN_NAME = "syndicate-bot"
private const val MODEL_NAME = "retinaface_anyshape.onnx"

val domainModule = module {
    single { CoroutineScope(Dispatchers.IO) }
    single<AppContext> { AppContext.getEnvironment(TOKEN_NAME) }

    factoryOf(::BotRepository)

    single { UserStateService() }

    single {
        val appContext = get<AppContext>()
        DesktopFaceDetectionService("${appContext.volumePath}$MODEL_NAME")
    }
    singleOf(::FaceFramesDrawer)
    singleOf(::FaceCountService)

    factoryOf(::GetOrInitUserStateUseCase)
    factoryOf(::ReceiveMessagesUseCase)
    factoryOf(::SendMessageUseCase)
    factoryOf(::UpdateUserStateUseCase)

    factoryOf(::HelpController)
    factoryOf(::RegisterController)
    factoryOf(::UndefinedController)
    factoryOf(::FaceCountController)

    factoryOf(::BotProcessor)
}
