package ru.kudryavtsev.domain.controller

import ru.kudryavtsev.ReadCsvUseCase
import ru.kudryavtsev.domain.AppContext
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
import ru.kudryavtsev.model.StudentRecord

class NewRegisterController(
    private val sendMessage: SendMessageUseCase,
    private val getStudent: GetStudentByTelegramIdUseCase,
    private val updateUserState: UpdateUserStateUseCase,
    private val registerStudent: RegisterStudentUseCase,
    csvReader: ReadCsvUseCase,
    context: AppContext,
) : IRegisterController {
    private val studentsRegistry: List<StudentRecord> = csvReader(context.studentRegistry)
    private val idRegex = "^2[0-9][А-Я][0-z]{3}$".toRegex()
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
            else -> error("Current implementation have one step")
        }
    }

    override fun sendDummyMessage(message: Message) {
        val newMessage = message.copy(text = DUMMY_MESSAGE)
        sendMessage(newMessage)
    }

    private fun UserState.Registering.processFirstStep(message: Message) {
        val studentId = message.text

        val studentRecord = parseMessage(studentId)

        if (studentRecord == null) {
            val newMessage = Message(
                userInfo = message.userInfo,
                text = REGISTER_FAILED
            )
            sendMessage(newMessage)
            return
        }

        val completeStudent = student.copy(
            name = studentRecord.name,
            group = studentRecord.group
        )

        registerStudent(completeStudent)

        updateUserState[message.userInfo.userId] = BaseUserState.Registered
        val newMessage = Message(
            userInfo = message.userInfo,
            text = REGISTER_SUCCESS
        )
        sendMessage(newMessage)
    }

    private fun parseMessage(id: String?): StudentRecord? {
        val upperId = id?.uppercase()
        upperId?.find(idRegex) ?: return null
        return studentsRegistry.firstOrNull { it.id == upperId }
    }

    companion object {
        private const val EMPTY_STRING = ""
        private const val REGISTERING_FIRST_MESSAGE = "Здравствуйте!\n\n" +
                "Бот разработан для автоматизации процесса отметки посещений студентов на дисциплинах ОП и ООП.\n\n" +
                "Для начала работы с ботом отправьте мне номер вашей зачётной книжки или студенческого билета"
    }
}
