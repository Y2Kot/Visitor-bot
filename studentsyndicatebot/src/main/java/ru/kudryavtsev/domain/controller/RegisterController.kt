package ru.kudryavtsev.domain.controller

import ru.kudryavtsev.domain.model.BaseUserState
import ru.kudryavtsev.domain.usecase.SendMessageUseCase
import ru.kudryavtsev.domain.model.Message
import ru.kudryavtsev.domain.usecase.UpdateUserStateUseCase

class RegisterController(
    private val sendMessage: SendMessageUseCase,
    private val updateUserState: UpdateUserStateUseCase,
) {
    fun registerUser(message: Message) {
        val newMessage = Message(
            userInfo = message.userInfo,
            text = REGISTERING_FIRST_MESSAGE
        )
        updateUserState[message.userInfo.userId] = BaseUserState.Registered
        sendMessage(newMessage)
    }

    companion object {
        private const val REGISTERING_FIRST_MESSAGE = "Здравствуйте!\n\n" +
                "Бот разработан для определения количества лиц на фотографиях. " +
                "Для начала - вызовите комманду count\n\n"
    }
}
