package ru.kudryavtsev.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.onEach
import ru.kudryavtsev.domain.command.BaseBotCommand
import ru.kudryavtsev.domain.controller.FaceCountController
import ru.kudryavtsev.domain.controller.HelpController
import ru.kudryavtsev.domain.controller.RegisterController
import ru.kudryavtsev.domain.controller.UndefinedController
import ru.kudryavtsev.domain.model.BaseUserState
import ru.kudryavtsev.domain.model.BotCommand
import ru.kudryavtsev.domain.usecase.ReceiveMessagesUseCase
import ru.kudryavtsev.domain.model.Message
import ru.kudryavtsev.domain.model.UserState
import ru.kudryavtsev.domain.usecase.GetOrInitUserStateUseCase

class BotProcessor(
    receiveMessages: ReceiveMessagesUseCase,
    private val getOrInitUserState: GetOrInitUserStateUseCase,
    private val registerController: RegisterController,
    private val faceCountController: FaceCountController,
    private val helpController: HelpController,
    private val undefinedController: UndefinedController,
) : BaseBotProcessor {
    override val messages: Flow<Message> = receiveMessages().onEach { message ->
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
            BotCommand.Count -> faceCountController.requestFaceDetection(message)
            BotCommand.Help -> helpController.sendHelpMessage(message)
            BaseBotCommand.Undefined -> undefinedController.sendUndefinedMessage(message)
        }
    }

    private fun processText(message: Message) {
        when (getOrInitUserState[message.userInfo.userId]) {
            BaseUserState.Registered -> undefinedController.sendDummyMessage(message)
            UserState.UploadingImage -> faceCountController.processImage(message)
            BaseUserState.Undefined -> undefinedController.sendUndefinedMessage(message)
        }
    }
}
