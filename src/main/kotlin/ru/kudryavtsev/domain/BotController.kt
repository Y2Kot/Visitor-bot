package ru.kudryavtsev.domain

import com.github.benmanes.caffeine.cache.Cache
import com.github.benmanes.caffeine.cache.Caffeine
import com.qoollo.logger.logw
import ru.kudryavtsev.domain.utils.CacheUtils.set
import kotlinx.coroutines.flow.onEach
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.kudryavtsev.domain.model.BotCommand
import ru.kudryavtsev.domain.model.BotState
import ru.kudryavtsev.domain.model.Discipline
import ru.kudryavtsev.domain.model.Group
import ru.kudryavtsev.domain.model.Message
import ru.kudryavtsev.domain.model.RegisteringStep
import ru.kudryavtsev.domain.model.Student
import ru.kudryavtsev.domain.model.Subject
import ru.kudryavtsev.domain.model.Visit
import ru.kudryavtsev.domain.model.VisitParserException
import ru.kudryavtsev.domain.repository.AdministratorRepository
import ru.kudryavtsev.domain.repository.BotRepository
import ru.kudryavtsev.domain.repository.StudentsRepository
import ru.kudryavtsev.domain.repository.VisitRepository
import ru.kudryavtsev.domain.utils.CacheUtils.getOrPut
import java.time.Duration
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class BotController : KoinComponent {
    private val botRepository: BotRepository by inject()
    private val studentsRepository: StudentsRepository by inject()
    private val visitRepository: VisitRepository by inject()
    private val administratorRepository: AdministratorRepository by inject()

    val messages = botRepository.receiveMessages().onEach { message: Message ->
        if (message.isCommand) {
            executeCommand(message)
        } else {
            processText(message)
        }
    }

    private val usersCache: Cache<Long, BotState> = Caffeine.newBuilder()
        .maximumSize(STUDENTS_LIMIT)
        .initialCapacity(STUDENTS_INITIAL)
        .expireAfterWrite(Duration.ofDays(STUDENT_KEEP_IN_CACHE))
        .build()

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
        when (val userState = usersCache.getOrPut(message.userInfo.userId) { BotState.Undefined }) {
            is BotState.AddingVisit -> userState.process(message)
            BotState.Registered -> process(message)
            is BotState.Registering -> userState.process(message)
            BotState.UploadingImage -> processUpload(message)
            BotState.Undefined -> process(message)
        }
    }

    private fun Message.visitOp() {
        usersCache[userInfo.userId] = BotState.AddingVisit(Discipline.Op)
        botRepository.sendMessage(this.copy(text = REGISTER_VISIT_INTRO))
    }

    private fun Message.visitOop() {
        usersCache[userInfo.userId] = BotState.AddingVisit(Discipline.Oop)
        botRepository.sendMessage(this.copy(text = REGISTER_VISIT_INTRO))
    }

    private fun Message.sendHelpMessage() {
        botRepository.sendMessage(this.copy(text = HELP_MESSAGE))
    }

    private fun Message.sendInfoMessage() {
        val student = studentsRepository.getUserById(this.userInfo.userId)
        val text = if (student == null) {
            UNKNOWN_USER
        } else {
            String.format(USER_INFO, student.name, student.group.ordinal + 1)
        }
        botRepository.sendMessage(this.copy(text = text))
    }

    private fun Message.uploadImage() {
        if (!administratorRepository.isAdministrator(userInfo.userId)) {
            botRepository.sendMessage(this.copy(text = NOT_ENOUGH_PERMISSIONS))
            return
        }
        usersCache[userInfo.userId] = BotState.UploadingImage
        botRepository.sendMessage(this.copy(text = UPLOAD_IMAGE))
    }

    private fun Message.sendUndefinedMessage() {
        botRepository.sendMessage(this.copy(text = UNDEFINED))
    }

    private fun Message.registerUser() {
        val isExist = studentsRepository.isStudentExist(userInfo.userId)
        if (isExist) {
            val newMessage = Message(
                userInfo = userInfo,
                text = STUDENT_ALREADY_EXIST
            )
            botRepository.sendMessage(newMessage)
            return
        }
        usersCache[userInfo.userId] = BotState.Registering(
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
        botRepository.sendMessage(newMessage)
    }

    private fun process(message: Message) {
        val newMessage = message.copy(text = UNDEFINED)
        botRepository.sendMessage(newMessage)
    }

    private fun processUpload(message: Message) {
        message.text ?: return
        val students = studentsRepository.getAllStudents()
        for (student in students) {
            botRepository.sendMessage(
                Message(
                    userInfo = message.userInfo.copy(
                        userId = student.userId,
                        chatId = student.chatId
                    ),
                    text = message.text
                )
            )
        }
        usersCache[message.userInfo.userId] = BotState.Registered
    }

    private fun BotState.Registering.process(message: Message) {
        when (registeringStep) {
            RegisteringStep.First -> processFirstStep(message)
            RegisteringStep.Second -> processSecondStep(message)
        }
    }

    private fun BotState.Registering.processFirstStep(message: Message) {
        val userName = message.text ?: TODO("Текст сообщения отсутствует")
        usersCache[message.userInfo.userId] = this.copy(
            registeringStep = RegisteringStep.Second,
            student = student.copy(name = userName)
        )
        val newMessage = Message(
            userInfo = message.userInfo,
            replyId = message.messageId,
            text = REGISTERING_SECOND_MESSAGE
        )
        botRepository.sendMessage(newMessage)
    }

    private fun BotState.Registering.processSecondStep(message: Message) {
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
            usersCache[message.userInfo.userId] = BotState.Registered
            studentsRepository.registerStudent(student)
            messageText = REGISTER_SUCCESS
        } else {
            messageText = REGISTER_FAILED
        }
        val newMessage = Message(
            userInfo = message.userInfo,
            text = messageText
        )
        botRepository.sendMessage(newMessage)
    }

    private fun BotState.AddingVisit.process(message: Message) {
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
        val student = studentsRepository.getUserById(message.userInfo.userId) ?: run {
            botRepository.sendMessage(message.copy(text = UNKNOWN_USER))
            return
        }
        val (date, number) = try {
            answerParser(message.text)
        } catch (e: RuntimeException) {
            botRepository.sendMessage(message.copy(text = e.localizedMessage))
            return
        }

        val visit = Visit(
            studentId = student.id,
            date = date,
            numberOnImage = number,
            subject = subject
        )
        visitRepository.registerVisit(visit)
        botRepository.sendMessage(
            message.copy(
                replyId = message.messageId,
                text = VISIT_REGISTER_SUCCEED
            )
        )
        usersCache[message.userInfo.userId] = BotState.Registered
    }

    private fun answerParser(text: String?): Pair<LocalDate, Int> {
        val studentAnswer = text ?: throw VisitParserException.InvalidInputData
        val (dateStr, number) = try {
            val data = studentAnswer.split("\n")
            if (data.size != 2) {
                throw VisitParserException.InvalidInputData
            } else {
                data
            }
        } catch (e: RuntimeException) {
            logw(throwable = e) { "Can't parse input data: $text" }
            throw VisitParserException.InvalidInputData
        }
        val date = try {
            val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
            LocalDate.parse(dateStr, formatter)
        } catch (e: RuntimeException) {
            logw(throwable = e) { "LocalDate parsing failed, input was: $dateStr" }
            throw VisitParserException.InvalidDateFormat
        }
        val numberValue = try {
            number.toInt()
        } catch (e: RuntimeException) {
            logw(throwable = e) { "Can't parse student data, input was: $number" }
            throw VisitParserException.InvalidPhotoNumber
        }
        return date to numberValue
    }

    companion object {
        private const val STUDENTS_INITIAL = 50
        private const val STUDENTS_LIMIT = 200L
        private const val STUDENT_KEEP_IN_CACHE = 1L

        private const val HELP_MESSAGE = "По вопросам работы бота обращайтесь в телеграмм - @Y2Kot."
//        private const val SERVICE_MESSAGE = "Комманда на ремонте, будет доступна позже."
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
