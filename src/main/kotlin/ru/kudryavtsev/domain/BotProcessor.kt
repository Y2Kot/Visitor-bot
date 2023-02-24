package ru.kudryavtsev.domain

import kotlinx.coroutines.flow.onEach
import ru.kudryavtsev.domain.controller.HelpController
import ru.kudryavtsev.domain.controller.InfoController
import ru.kudryavtsev.domain.controller.RegisterController
import ru.kudryavtsev.domain.controller.UndefinedController
import ru.kudryavtsev.domain.controller.UploadImageController
import ru.kudryavtsev.domain.controller.VisitDisciplineController
import ru.kudryavtsev.domain.model.BotCommand
import ru.kudryavtsev.domain.model.Message
import ru.kudryavtsev.domain.model.StudentState
import ru.kudryavtsev.domain.usecase.GetOrInitStudentStateUseCase
import ru.kudryavtsev.domain.usecase.ReceiveMessagesUseCase

class BotProcessor(
    private val getOrInitStudentState: GetOrInitStudentStateUseCase,
    receiveMessages: ReceiveMessagesUseCase,
    private val helpController: HelpController,
    private val registerController: RegisterController,
    private val infoController: InfoController,
    private val undefinedController: UndefinedController,
    private val uploadImageController: UploadImageController,
    private val visitDisciplineController: VisitDisciplineController
) {

    val messages = receiveMessages().onEach { message: Message ->
        if (message.isCommand) {
            executeCommand(message)
        } else {
            processText(message)
        }
    }

    private fun executeCommand(message: Message) {
        message.text ?: return
        when (BotCommand.defineCommand(message.text)) {
            BotCommand.VisitOop -> visitDisciplineController.visitOp(message)
            BotCommand.VisitOp -> visitDisciplineController.visitOop(message)
            BotCommand.Start -> registerController.registerUser(message)
            BotCommand.Help -> helpController.sendHelpMessage(message)
            BotCommand.Info -> infoController.sendInfoMessage(message)
            BotCommand.UploadImage -> uploadImageController.uploadImage(message)
            BotCommand.Undefined -> undefinedController.sendUndefinedMessage(message)
        }
    }

    private fun processText(message: Message) {
        when (val userState = getOrInitStudentState[message.userInfo.userId]) {
            is StudentState.AddingVisit -> visitDisciplineController.process(message, userState.discipline)
            StudentState.Registered -> registerController.sendDummyMessage(message)
            is StudentState.Registering -> registerController.processRegistration(message, userState)
            StudentState.UploadingImage -> uploadImageController.processUpload(message)
            StudentState.Undefined -> undefinedController.sendUndefinedMessage(message)
        }
    }
}
