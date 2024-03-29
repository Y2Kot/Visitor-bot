package ru.kudryavtsev.domain.controller

import ru.kudryavtsev.domain.controller.IRegisterController.Companion.DUMMY_MESSAGE
import ru.kudryavtsev.domain.controller.IRegisterController.Companion.REGISTER_FAILED
import ru.kudryavtsev.domain.controller.IRegisterController.Companion.REGISTER_SUCCESS
import ru.kudryavtsev.domain.controller.IRegisterController.Companion.STUDENT_ALREADY_EXIST
import ru.kudryavtsev.domain.model.BaseUserState
import ru.kudryavtsev.domain.model.Message
import ru.kudryavtsev.domain.model.RegisteringStep
import ru.kudryavtsev.domain.model.Student
import ru.kudryavtsev.domain.model.UserState
import ru.kudryavtsev.domain.usecase.GetStudentByTelegramIdUseCase
import ru.kudryavtsev.domain.usecase.RegisterStudentUseCase
import ru.kudryavtsev.domain.usecase.SendMessageUseCase
import ru.kudryavtsev.domain.usecase.UpdateUserStateUseCase
import ru.kudryavtsev.domain.util.find

class RegisterController(
    private val sendMessage: SendMessageUseCase,
    private val getStudent: GetStudentByTelegramIdUseCase,
    private val updateUserState: UpdateUserStateUseCase,
    private val registerStudent: RegisterStudentUseCase
): IRegisterController {
    private val groupRegex = "^СГН3-[0-9]{2}Б$".toRegex()
    override fun registerUser(message: Message) {
        val student = getStudent[message.userInfo.userId]
        if (student != null) {
            val newMessage = Message(
                userInfo = message.userInfo,
                text = STUDENT_ALREADY_EXIST
            )
            sendMessage(newMessage)
            return
        }
        updateUserState[message.userInfo.userId] = UserState.Registering(
            student = Student(
                userId = message.userInfo.userId,
                chatId = message.userInfo.chatId,
                name = EMPTY_STRING,
                group = EMPTY_STRING
            )
        )
        val newMessage = Message(
            userInfo = message.userInfo,
            text = REGISTERING_FIRST_MESSAGE
        )
        sendMessage(newMessage)
    }

    override fun processRegistration(message: Message, registerState: UserState.Registering) {
        when (registerState.registeringStep) {
            RegisteringStep.First -> registerState.processFirstStep(message)
            RegisteringStep.Second -> registerState.processSecondStep(message)
        }
    }

    override fun sendDummyMessage(message: Message) {
        val newMessage = message.copy(text = DUMMY_MESSAGE)
        sendMessage(newMessage)
    }

    private fun UserState.Registering.processFirstStep(message: Message) {
        val userName = message.text ?: TODO("Текст сообщения отсутствует")
        updateUserState[message.userInfo.userId] = this.copy(
            registeringStep = RegisteringStep.Second,
            student = student.copy(name = userName)
        )
        sendMessage(
            Message(
                userInfo = message.userInfo,
                replyId = message.messageId,
                text = REGISTERING_SECOND_MESSAGE
            )
        )
    }

    private fun UserState.Registering.processSecondStep(message: Message) {
        val groupText = message.text?.find(groupRegex)

        val messageText: String
        if (!groupText.isNullOrEmpty()) {
            val student = student.copy(group = groupText)
            registerStudent(student)
            updateUserState[message.userInfo.userId] = BaseUserState.Registered
            messageText = REGISTER_SUCCESS
        } else {
            messageText = REGISTER_FAILED
        }
        val newMessage = Message(
            userInfo = message.userInfo,
            text = messageText
        )
        sendMessage(newMessage)
    }

    companion object {
        private const val REGISTERING_FIRST_MESSAGE = "Здравствуйте!\n\n" +
                "Бот разработан для автоматизации процесса отметки посещений студентов на дисциплинах ОП и ООП.\n\n" +
                "Для активации бота необходимо ответить на 2 вопроса:\n\n" +
                "1. Фамилия и инициалы (Формат: Иванов И И)."
        private const val REGISTERING_SECOND_MESSAGE = "2. Группа (Формат: СГН3-14Б).\n\n" +
                "*Важно!* Название кафедры или номер семестра - *указывать не нужно*."

        private const val EMPTY_STRING = ""
    }
}
