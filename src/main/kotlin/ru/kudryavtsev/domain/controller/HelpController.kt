package ru.kudryavtsev.domain.controller

import ru.kudryavtsev.domain.model.Message
import ru.kudryavtsev.domain.usecase.SendMessageUseCase

class HelpController(private val sendMessage: SendMessageUseCase) {
    fun sendHelpMessage(message: Message) {
        sendMessage(message.copy(text = HELP_MESSAGE))
    }

    companion object {
        private const val HELP_MESSAGE = "По вопросам работы бота обращайтесь в телеграмм - @Y2Kot."
    }
}
