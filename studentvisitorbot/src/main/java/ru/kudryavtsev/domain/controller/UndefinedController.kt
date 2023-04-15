package ru.kudryavtsev.domain.controller

import ru.kudryavtsev.model.Message
import ru.kudryavtsev.domain.usecase.SendMessageUseCase

class UndefinedController(private val sendMessage: SendMessageUseCase) {
    fun sendUndefinedMessage(message: Message) {
        sendMessage(message.copy(text = UNDEFINED))
    }

    companion object {
        private const val UNDEFINED = "Получено неизвестное сообщение"
    }
}
