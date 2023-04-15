package ru.kudryavtsev.domain.controller

import ru.kudryavtsev.model.Message
import ru.kudryavtsev.domain.usecase.GetStudentUseCase
import ru.kudryavtsev.domain.usecase.SendMessageUseCase

class InfoController(
    private val getStudent: GetStudentUseCase,
    private val sendMessage: SendMessageUseCase
) {
    fun sendInfoMessage(message: Message) {
        val student = getStudent[message.userInfo.userId]
        val text = if (student == null) {
            UNKNOWN_USER
        } else {
            String.format(USER_INFO, student.name, student.group.ordinal + 1)
        }
        sendMessage(message.copy(text = text))
    }

    companion object {
        private const val USER_INFO = "Имя пользователя: %s\nГруппа: %d"
        private const val UNKNOWN_USER = "Увы, но кажется мы не знакомы \uD83D\uDE1E"
    }
}
