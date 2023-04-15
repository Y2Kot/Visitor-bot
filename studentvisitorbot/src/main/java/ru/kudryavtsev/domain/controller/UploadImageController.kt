package ru.kudryavtsev.domain.controller

import ru.kudryavtsev.model.Message
import ru.kudryavtsev.domain.model.StudentState
import ru.kudryavtsev.domain.usecase.CheckAdminPermissionUseCase
import ru.kudryavtsev.domain.usecase.GetAllStudentsUseCase
import ru.kudryavtsev.domain.usecase.SendMessageUseCase
import ru.kudryavtsev.domain.usecase.UpdateStudentStateUseCase

class UploadImageController(
    private val checkAdminPermission: CheckAdminPermissionUseCase,
    private val sendMessage: SendMessageUseCase,
    private val updateStudentState: UpdateStudentStateUseCase,
    private val getAllStudents: GetAllStudentsUseCase
) {
    fun uploadImage(message: Message) {
        if (!checkAdminPermission(message.userInfo.userId)) {
            sendMessage(message.copy(text = NOT_ENOUGH_PERMISSIONS))
            return
        }
        updateStudentState[message.userInfo.userId] = StudentState.UploadingImage
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
        updateStudentState[message.userInfo.userId] = StudentState.Registered
    }

    companion object {
        private const val UPLOAD_IMAGE = "Загрузите файл:"

        private const val NOT_ENOUGH_PERMISSIONS = "У вас недостаточно прав для выполнения операции!"
    }
}
