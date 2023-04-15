package ru.kudryavtsev.domain.controller

import ru.kudryavtsev.domain.model.Group
import ru.kudryavtsev.model.Message
import ru.kudryavtsev.domain.model.RegisteringStep
import ru.kudryavtsev.domain.model.Student
import ru.kudryavtsev.domain.model.StudentState
import ru.kudryavtsev.domain.usecase.GetStudentUseCase
import ru.kudryavtsev.domain.usecase.RegisterStudentUseCase
import ru.kudryavtsev.domain.usecase.SendMessageUseCase
import ru.kudryavtsev.domain.usecase.UpdateStudentStateUseCase

class RegisterController(
    private val sendMessage: SendMessageUseCase,
    private val getStudent: GetStudentUseCase,
    private val updateStudentState: UpdateStudentStateUseCase,
    private val registerStudent: RegisterStudentUseCase
) {
    fun registerUser(message: Message) {
        val student = getStudent[message.userInfo.userId]
        if (student != null) {
            val newMessage = Message(
                userInfo = message.userInfo,
                text = STUDENT_ALREADY_EXIST
            )
            sendMessage(newMessage)
            return
        }
        updateStudentState[message.userInfo.userId] = StudentState.Registering(
            student = Student(
                userId = message.userInfo.userId,
                chatId = message.userInfo.chatId,
                name = EMPTY_STRING,
                group = Group.UNDEFINED
            )
        )
        val newMessage = Message(
            userInfo = message.userInfo,
            text = REGISTERING_FIRST_MESSAGE
        )
        sendMessage(newMessage)
    }

    fun processRegistration(message: Message, registerState: StudentState.Registering) {
        when (registerState.registeringStep) {
            RegisteringStep.First -> registerState.processFirstStep(message)
            RegisteringStep.Second -> registerState.processSecondStep(message)
        }
    }

    fun sendDummyMessage(message: Message) {
        val newMessage = message.copy(text = DUMMY_MESSAGE)
        sendMessage(newMessage)
    }

    private fun StudentState.Registering.processFirstStep(message: Message) {
        val userName = message.text ?: TODO("Текст сообщения отсутствует")
        updateStudentState[message.userInfo.userId] = this.copy(
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

    private fun StudentState.Registering.processSecondStep(message: Message) {
        val textMessage = message.text ?: TODO("Текст сообщения отсутствует")
        val userGroup = try {
            val groupNumber = textMessage.toInt() - 1
            Group.values()[groupNumber]
        } catch (e: RuntimeException) {
            println("Wrong group number")
            Group.UNDEFINED
        }
        val messageText: String
        if (userGroup != Group.UNDEFINED) {
            val student = student.copy(group = userGroup)
            registerStudent(student)
            updateStudentState[message.userInfo.userId] = StudentState.Registered
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
        private const val REGISTERING_SECOND_MESSAGE = "2. Группа (Формат: 4).\n\n" +
                "*Важно!* Название кафедры или номер семестра - *указывать не нужно*."

        private const val REGISTER_SUCCESS = "Пользователь успешно зарегистрирован."
        private const val REGISTER_FAILED = "Шалунишка! Введи пожалуйста правильное значение!"
        private const val STUDENT_ALREADY_EXIST = "Мы уже знакомы ❤️\n" +
                "Если ошиблись при написании регистрационных данных - обратитесь к лектору - @Y2Kot. " +
                "Ручное редактирование пока не доступно."
        private const val DUMMY_MESSAGE = "Для работы с ботом воспользуйтесь меню команд."
        private const val EMPTY_STRING = ""
    }
}
