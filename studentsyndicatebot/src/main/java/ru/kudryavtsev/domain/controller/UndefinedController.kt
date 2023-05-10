package ru.kudryavtsev.domain.controller

import ru.kudryavtsev.domain.model.Message
import ru.kudryavtsev.domain.usecase.SendMessageUseCase

class UndefinedController(private val sendMessage: SendMessageUseCase) {
    fun sendUndefinedMessage(message: Message) {
        sendMessage(message.copy(text = UNDEFINED))
    }

    fun sendDummyMessage(message: Message) {
        val newMessage = message.copy(text = DUMMY_MESSAGE)
        sendMessage(newMessage)
    }

    companion object {
        private const val UNDEFINED = "Получено неизвестное сообщение"
        private const val DUMMY_MESSAGE = "Для работы с ботом воспользуйтесь меню команд."
    }
}
