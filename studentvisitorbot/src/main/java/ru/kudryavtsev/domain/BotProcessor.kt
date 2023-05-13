package ru.kudryavtsev.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import ru.kudryavtsev.domain.command.BaseBotCommand
import ru.kudryavtsev.domain.controller.HelpController
import ru.kudryavtsev.domain.controller.IRegisterController
import ru.kudryavtsev.domain.controller.InfoController
import ru.kudryavtsev.domain.controller.UndefinedController
import ru.kudryavtsev.domain.controller.UploadImageController
import ru.kudryavtsev.domain.controller.VisitDisciplineController
import ru.kudryavtsev.domain.model.BaseUserState
import ru.kudryavtsev.domain.model.BotCommand
import ru.kudryavtsev.domain.model.Message
import ru.kudryavtsev.domain.model.UserState
import ru.kudryavtsev.domain.usecase.GetOrInitUserStateUseCase
import ru.kudryavtsev.domain.usecase.ReceiveMessagesUseCase

class BotProcessor(
    private val getOrInitUserState: GetOrInitUserStateUseCase,
    receiveMessages: ReceiveMessagesUseCase,
    private val helpController: HelpController,
    private val registerController: IRegisterController,
    private val infoController: InfoController,
    private val undefinedController: UndefinedController,
    private val uploadImageController: UploadImageController,
    private val visitDisciplineController: VisitDisciplineController
): BaseBotProcessor {

    override val messages: Flow<Message> = receiveMessages().onEach { message: Message ->
        if (message.isCommand) {
            executeCommand(message)
        } else {
            processText(message)
        }
    }

    private fun executeCommand(message: Message) {
        val messageText = message.text ?: return
        when (BotCommand.defineCommand(messageText)) {
            BaseBotCommand.Start -> registerController.registerUser(message)
            BotCommand.VisitOopLecture -> visitDisciplineController.visitOopLecture(message)
            BotCommand.VisitOopLab -> visitDisciplineController.visitOopLab(message)
            BotCommand.VisitOpLecture -> visitDisciplineController.visitOpLecture(message)
            BotCommand.VisitOpLab -> visitDisciplineController.visitOpLab(message)
            BotCommand.Help -> helpController.sendHelpMessage(message)
            BotCommand.Info -> infoController.sendInfoMessage(message)
            BotCommand.UploadImage -> uploadImageController.uploadImage(message)
            BaseBotCommand.Undefined -> undefinedController.sendUndefinedMessage(message)
        }
    }

    private fun processText(message: Message) {
        when (val userState = getOrInitUserState[message.userInfo.userId]) {
            is UserState.AddingVisit -> visitDisciplineController.process(message, userState.discipline)
            BaseUserState.Registered -> registerController.sendDummyMessage(message)
            is UserState.Registering -> registerController.processRegistration(message, userState)
            UserState.UploadingImage -> uploadImageController.processUpload(message)
            BaseUserState.Undefined -> undefinedController.sendUndefinedMessage(message)
        }
    }
}
