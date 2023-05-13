package ru.kudryavtsev.domain.controller

import ru.kudryavtsev.domain.model.Message
import ru.kudryavtsev.domain.usecase.GetStudentByTelegramIdUseCase
import ru.kudryavtsev.domain.usecase.SendMessageUseCase

class InfoController(
    private val getStudent: GetStudentByTelegramIdUseCase,
    private val sendMessage: SendMessageUseCase
) {
    fun sendInfoMessage(message: Message) {
        val student = getStudent[message.userInfo.userId]
        val text = if (student == null) {
            UNKNOWN_USER
        } else {
            String.format(USER_INFO, student.name, student.group)
        }
        sendMessage(message.copy(text = text))
    }

    companion object {
        private const val USER_INFO = "Имя пользователя: %s\nГруппа: %s"
        private const val UNKNOWN_USER = "Увы, но кажется мы не знакомы \uD83D\uDE1E"
    }
}
