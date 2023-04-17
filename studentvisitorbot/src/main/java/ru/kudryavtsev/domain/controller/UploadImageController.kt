package ru.kudryavtsev.domain.controller

import ru.kudryavtsev.domain.model.BaseUserState
import ru.kudryavtsev.domain.model.Message
import ru.kudryavtsev.domain.model.UserState
import ru.kudryavtsev.domain.usecase.CheckAdminPermissionUseCase
import ru.kudryavtsev.domain.usecase.GetAllStudentsUseCase
import ru.kudryavtsev.domain.usecase.SendMessageUseCase
import ru.kudryavtsev.domain.usecase.UpdateUserStateUseCase

class UploadImageController(
    private val checkAdminPermission: CheckAdminPermissionUseCase,
    private val sendMessage: SendMessageUseCase,
    private val updateUserState: UpdateUserStateUseCase,
    private val getAllStudents: GetAllStudentsUseCase
) {
    fun uploadImage(message: Message) {
        if (!checkAdminPermission(message.userInfo.userId)) {
            sendMessage(message.copy(text = NOT_ENOUGH_PERMISSIONS))
            return
        }
        updateUserState[message.userInfo.userId] = UserState.UploadingImage
        sendMessage(message.copy(text = UPLOAD_IMAGE))
    }

    fun processUpload(message: Message) {
        message.text ?: return
        getAllStudents().forEach { student ->
            sendMessage(
                Message(
                    userInfo = message.userInfo.copy(
                        userId = student.userId,
                        chatId = student.chatId
                    ),
                    text = message.text
                )
            )
        }
        updateUserState[message.userInfo.userId] = BaseUserState.Registered
    }

    companion object {
        private const val UPLOAD_IMAGE = "Загрузите файл:"

        private const val NOT_ENOUGH_PERMISSIONS = "У вас недостаточно прав для выполнения операции!"
    }
}
