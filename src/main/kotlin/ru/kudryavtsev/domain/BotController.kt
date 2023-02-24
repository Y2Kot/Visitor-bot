package ru.kudryavtsev.domain

import com.qoollo.logger.logw
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.KoinComponent
import ru.kudryavtsev.domain.model.BotCommand
import ru.kudryavtsev.domain.model.Discipline
import ru.kudryavtsev.domain.model.Group
import ru.kudryavtsev.domain.model.Message
import ru.kudryavtsev.domain.model.RegisteringStep
import ru.kudryavtsev.domain.model.Student
import ru.kudryavtsev.domain.model.StudentState
import ru.kudryavtsev.domain.model.Subject
import ru.kudryavtsev.domain.model.Visit
import ru.kudryavtsev.domain.usecase.CheckAdminPermissionUseCase
import ru.kudryavtsev.domain.usecase.GetAllStudentsUseCase
import ru.kudryavtsev.domain.usecase.GetOrInitStudentStateUseCase
import ru.kudryavtsev.domain.usecase.GetStudentUseCase
import ru.kudryavtsev.domain.usecase.ReceiveMessagesUseCase
import ru.kudryavtsev.domain.usecase.RegisterStudentUseCase
import ru.kudryavtsev.domain.usecase.RegisterVisitUseCase
import ru.kudryavtsev.domain.usecase.SendMessageUseCase
import ru.kudryavtsev.domain.usecase.UpdateStudentStateUseCase
import ru.kudryavtsev.domain.util.answerParser

class BotController(
    private val sendMessage: SendMessageUseCase,
    private val getOrInitStudentState: GetOrInitStudentStateUseCase,
    private val updateStudentState: UpdateStudentStateUseCase,
    private val getAllStudents: GetAllStudentsUseCase,
    private val registerStudent: RegisterStudentUseCase,
    private val getStudent: GetStudentUseCase,
    private val checkAdminPermission: CheckAdminPermissionUseCase,
    private val registerVisit: RegisterVisitUseCase,
    receiveMessages: ReceiveMessagesUseCase
) : KoinComponent {
//    private val visitRepository: VisitRepository by inject()
//    private val administratorRepository: AdministratorRepository by inject()

    val messages = receiveMessages().onEach { message: Message ->
        if (message.isCommand) {
            executeCommand(message)
        } else {
            processText(message)
        }
    }

    private fun executeCommand(message: Message) {
        message.text ?: return
        when (BotCommand.defineCommand(message.text)) {
            BotCommand.VisitOop -> message.visitOp()
            BotCommand.VisitOp -> message.visitOop()
            BotCommand.Start -> message.registerUser()
            BotCommand.Help -> message.sendHelpMessage()
            BotCommand.Info -> message.sendInfoMessage()
            BotCommand.UploadImage -> message.uploadImage()
            BotCommand.Undefined -> message.sendUndefinedMessage()
        }
    }

    private fun processText(message: Message) {
        when (val userState = getOrInitStudentState[message.userInfo.userId]) {
            is StudentState.AddingVisit -> userState.process(message)
            StudentState.Registered -> process(message)
            is StudentState.Registering -> userState.process(message)
            StudentState.UploadingImage -> processUpload(message)
            StudentState.Undefined -> process(message)
        }
    }

    private fun Message.visitOp() {
        updateStudentState[userInfo.userId] = StudentState.AddingVisit(Discipline.Op)
        sendMessage(this.copy(text = REGISTER_VISIT_INTRO))
    }

    private fun Message.visitOop() {
        updateStudentState[userInfo.userId] = StudentState.AddingVisit(Discipline.Oop)
        sendMessage(this.copy(text = REGISTER_VISIT_INTRO))
    }

    private fun Message.sendHelpMessage() {
        sendMessage(this.copy(text = HELP_MESSAGE))
    }

    private fun Message.sendInfoMessage() {
        val student = getStudent[this.userInfo.userId]
        val text = if (student == null) {
            UNKNOWN_USER
        } else {
            String.format(USER_INFO, student.name, student.group.ordinal + 1)
        }
        sendMessage(this.copy(text = text))
    }

    private fun Message.uploadImage() {
        if (!checkAdminPermission(userInfo.userId)) {
            sendMessage(this.copy(text = NOT_ENOUGH_PERMISSIONS))
            return
        }
        updateStudentState[userInfo.userId] = StudentState.UploadingImage
        sendMessage(this.copy(text = UPLOAD_IMAGE))
    }

    private fun Message.sendUndefinedMessage() {
        sendMessage(this.copy(text = UNDEFINED))
    }

    private fun Message.registerUser() {
        val student = getStudent[userInfo.userId]
        if (student != null) {
            val newMessage = Message(
                userInfo = userInfo,
                text = STUDENT_ALREADY_EXIST
            )
            sendMessage(newMessage)
            return
        }
        updateStudentState[userInfo.userId] = StudentState.Registering(
            student = Student(
                userId = userInfo.userId,
                chatId = userInfo.chatId,
                name = EMPTY_STRING,
                group = Group.UNDEFINED
            )
        )
        val newMessage = Message(
            userInfo = userInfo,
            text = REGISTERING_FIRST_MESSAGE
        )
        sendMessage(newMessage)
    }

    private fun process(message: Message) {
        val newMessage = message.copy(text = UNDEFINED)
        sendMessage(newMessage)
    }

    private fun processUpload(message: Message) {
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

    private fun StudentState.Registering.process(message: Message) {
        when (registeringStep) {
            RegisteringStep.First -> processFirstStep(message)
            RegisteringStep.Second -> processSecondStep(message)
        }
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
        message.text ?: TODO("Текст сообщения отсутствует")
        val userGroup = try {
            val groupNumber = message.text.toInt() - 1
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

    private fun StudentState.AddingVisit.process(message: Message) {
        when (discipline) {
            Discipline.Oop -> processOpVisit(message)
            Discipline.Op -> processOopVisit(message)
        }
    }

    private fun processOpVisit(message: Message) {
        processVisit(message, Subject.OP)
    }

    private fun processOopVisit(message: Message) {
        processVisit(message, Subject.OOP)
    }

    private fun processVisit(message: Message, subject: Subject) {
        val student = getStudent[message.userInfo.userId] ?: run {
            sendMessage(message.copy(text = UNKNOWN_USER))
            return
        }
        val visitPayload = try {
            message.text.answerParser()
        } catch (e: RuntimeException) {
            logw(throwable = e) { "Visit payload parsing failed with message " }
            sendMessage(message.copy(text = e.localizedMessage))
            return
        }

        val visit = Visit(
            studentId = student.id,
            date = visitPayload.date,
            numberOnImage = visitPayload.number,
            subject = subject
        )
        registerVisit(visit)
        sendMessage(
            message.copy(
                replyId = message.messageId,
                text = VISIT_REGISTER_SUCCEED
            )
        )
        updateStudentState[message.userInfo.userId] = StudentState.Registered
    }

    companion object {

        private const val HELP_MESSAGE = "По вопросам работы бота обращайтесь в телеграмм - @Y2Kot."

        //        private const val SERVICE_MESSAGE = "Команда на ремонте, будет доступна позже."
        private const val UNDEFINED = "Получено неизвестное сообщение"

        private const val REGISTERING_FIRST_MESSAGE = "Здравствуйте!\n\n" +
                "Бот разработн для автоматизации процесса отметки посещений студентов на дисциплинах ОП и ООП.\n\n" +
                "Для активации бота необходимо ответить на 2 вопроса:\n\n" +
                "1. Фамилия и инициалы (Формат: Иванов И И)."
        private const val REGISTERING_SECOND_MESSAGE = "2. Группа (Формат: 4).\n\n" +
                "*Важно!* Название кафедры или номер семестра - *указывать не нужно*."

        private const val REGISTER_SUCCESS = "Пользователь успешно зарегистрирован."
        private const val REGISTER_FAILED = "Шалунишка! Введи пожалуйста правильное значение!"
        private const val STUDENT_ALREADY_EXIST = "Мы уже знакомы ❤️\n" +
                "Если ошиблись при написании регистрационных данных - обратитесь к лектору - @Y2Kot. " +
                "Ручное редактирование пока не доступно."

        private const val USER_INFO = "Имя пользователя: %s\nГруппа: %d"
        private const val UNKNOWN_USER = "Увы, но кажется мы не знакомы \uD83D\uDE1E"

        private const val REGISTER_VISIT_INTRO =
            "Если хочешь отметиться на паре, пришли мне следующую информацию: " +
                    "дата посещения, номер на фото (каждый пункт с новой строки).\n\n" +
                    "*Например:*\n" +
                    "26.02.2022\n" +
                    "43"
        private const val VISIT_REGISTER_SUCCEED = "Посещение получено!"

        private const val UPLOAD_IMAGE = "Загрузите файл:"

        private const val EMPTY_STRING = ""

        private const val NOT_ENOUGH_PERMISSIONS = "У вас недостаточно прав для выполнения операции!"
    }
}
