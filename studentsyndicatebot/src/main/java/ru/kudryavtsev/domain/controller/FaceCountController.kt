package ru.kudryavtsev.domain.controller

import ru.kudryavtsev.domain.FaceCountService
import ru.kudryavtsev.domain.model.BaseUserState
import ru.kudryavtsev.domain.model.Message
import ru.kudryavtsev.domain.model.UserState
import ru.kudryavtsev.domain.usecase.SendMessageUseCase
import ru.kudryavtsev.domain.usecase.UpdateUserStateUseCase

class FaceCountController(
    private val sendMessage: SendMessageUseCase,
    private val updateUserState: UpdateUserStateUseCase,
    private val countService: FaceCountService
) {
    fun requestFaceDetection(message: Message) {
        val isExist = countService.isRequestExist(message.userInfo)
        sendMessage(
            message.copy(
                text = if (isExist) REQUEST_ALREADY_EXIST else COUNT_PEOPLE_INTRO
            )
        )
        if (!isExist) {
            updateUserState[message.userInfo.userId] = UserState.UploadingImage
        }
    }

    fun processImage(message: Message) {
        val receivedImage = message.attachment
        sendMessage(
            message.copy(
                replyId = message.messageId,
                text = if (receivedImage != null) REQUEST_RECEIVED else WRONGED_IMAGE_RECEIVED,
                attachment = null
            )
        )
        if (receivedImage == null) {
            return
        }
        countService.addRequest(message.userInfo, receivedImage) { recognisedFaces, processedImage ->
            sendMessage(
                message.copy(
                    text = String.format(DETECTION_RESULT, recognisedFaces),
                    attachment = processedImage
                )
            )
            processedImage.deleteOnExit()
            updateUserState[message.userInfo.userId] = BaseUserState.Registered
        }
    }

    companion object {
        private const val COUNT_PEOPLE_INTRO = "Загрузите фото без сжатия (в качестве документа) " +
                "для определения количества лиц на нём."
        private const val REQUEST_ALREADY_EXIST = "Запрос уже находится в обработке. " +
                "Пока бот умеет обрабатывать только один запрос за раз."
        private const val WRONGED_IMAGE_RECEIVED =
            "Документ не обнаружен. Попробуйте загрузить изображение как документ"
        private const val REQUEST_RECEIVED = "Изображение получено, ожидайте результат. " +
                "По готовности вы получите сообщение с количеством обнаруженных лиц и фотографию с обнаруженными лицами"
        private const val DETECTION_RESULT = "Обнаружено: %d лиц"
    }
}
