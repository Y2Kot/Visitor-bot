package ru.kudryavtsev.domain.controller

import com.qoollo.logger.loge
import ru.kudryavtsev.domain.interactor.RegisterVisitInteractor
import ru.kudryavtsev.domain.model.BaseUserState
import ru.kudryavtsev.domain.model.Discipline
import ru.kudryavtsev.domain.model.Message
import ru.kudryavtsev.domain.model.Subject
import ru.kudryavtsev.domain.model.UserState
import ru.kudryavtsev.domain.model.Visit
import ru.kudryavtsev.domain.usecase.GetStudentByTelegramIdUseCase
import ru.kudryavtsev.domain.usecase.GetVisitsByDateUseCase
import ru.kudryavtsev.domain.usecase.SendMessageUseCase
import ru.kudryavtsev.domain.usecase.UpdateUserStateUseCase
import ru.kudryavtsev.domain.util.answerParser

class VisitDisciplineController(
    private val updateStudentState: UpdateUserStateUseCase,
    private val sendMessage: SendMessageUseCase,
    private val getStudent: GetStudentByTelegramIdUseCase,
    private val registerVisit: RegisterVisitInteractor,
    private val getVisitsByDate: GetVisitsByDateUseCase,
) {
    fun visitOpLecture(message: Message) {
        visitDiscipline(message, Discipline.OpLecture) {
            updateStudentState[message.userInfo.userId] = UserState.AddingVisit(Discipline.OpLecture)
        }
    }

    fun visitOpLab(message: Message) {
        visitDiscipline(message, Discipline.OpLab) {
            updateStudentState[message.userInfo.userId] = UserState.AddingVisit(Discipline.OpLab)
        }
    }

    fun visitOopLecture(message: Message) {
        visitDiscipline(message, Discipline.OopLecture) {
            updateStudentState[message.userInfo.userId] = UserState.AddingVisit(Discipline.OopLecture)
        }
    }

    fun visitOopLab(message: Message) {
        visitDiscipline(message, Discipline.OopLab) {
            updateStudentState[message.userInfo.userId] = UserState.AddingVisit(Discipline.OopLab)
        }
    }

    fun process(message: Message, discipline: Discipline) {
        if (isCancellationMessage(message)) {
            processCancellation(message)
            return
        }
        when (discipline) {
            Discipline.OpLecture -> processOpLectureVisit(message)
            Discipline.OpLab -> processOpLabVisit(message)
            Discipline.OopLecture -> processOopLectureVisit(message)
            Discipline.OopLab -> processOopLabVisit(message)
        }
    }

    private fun isCancellationMessage(message: Message): Boolean =
        message.text?.equals(CANCEL_COMMAND) ?: false

    private fun processCancellation(message: Message) {
        sendMessage(
            message.copy(
                replyId = message.messageId,
                text = REQUEST_CANCELED
            )
        )
        updateStudentState[message.userInfo.userId] = BaseUserState.Registered
    }

    private fun visitDiscipline(message: Message, discipline: Discipline, block: () -> Unit) {
        block()
        val textMessage = String.format(REGISTER_VISIT_INTRO, discipline.description)
        sendMessage(message.copy(text = textMessage))
    }

    private fun processOpLectureVisit(message: Message) {
        processVisit(message, Subject.OP_LECTURE)
    }

    private fun processOpLabVisit(message: Message) {
        processVisit(message, Subject.OP_LAB)
    }

    private fun processOopLectureVisit(message: Message) {
        processVisit(message, Subject.OOP_LECTURE)
    }

    private fun processOopLabVisit(message: Message) {
        processVisit(message, Subject.OOP_LAB)
    }

    private fun processVisit(message: Message, subject: Subject) {
        val student = getStudent[message.userInfo.userId] ?: run {
            sendMessage(message.copy(text = UNKNOWN_USER))
            return
        }
        val visitPayload = try {
            message.text.answerParser()
        } catch (e: RuntimeException) {
            loge(throwable = e) {
                "Visit payload parsing failed. " +
                        "User id: ${message.userInfo.userId}, text message was: ${message.text}"
            }
            sendMessage(message.copy(text = e.localizedMessage))
            return
        }

        val isAlreadyVisited = getVisitsByDate(visitPayload.date)
            .any {
                val isDisciplineTheSame = it.subject == subject
                val isNumberExist = visitPayload.number == it.numberOnImage
                isDisciplineTheSame && isNumberExist
            }
        if (isAlreadyVisited) {
            sendMessage(message.copy(text = VISIT_ALREADY_EXIST))
            return
        }

        val visit = Visit(
            studentId = student.id,
            date = visitPayload.date,
            numberOnImage = visitPayload.number,
            subject = subject
        )
        try {
            registerVisit(visit)
            sendMessage(
                message.copy(
                    replyId = message.messageId,
                    text = VISIT_REGISTER_SUCCEED
                )
            )
            updateStudentState[message.userInfo.userId] = BaseUserState.Registered
        } catch (e: RuntimeException) {
            loge(throwable = e) {
                "Registration failed, " +
                        "user: ${message.userInfo.userId}, subject: ${subject.name}, text message: ${message.text}"
            }
            sendMessage(
                message.copy(
                    replyId = message.messageId,
                    text = VISIT_REGISTER_ERROR
                )
            )
        }
    }


    companion object {
        private const val CANCEL_COMMAND = "Отмена"
        private const val REQUEST_CANCELED = "Запрос на регистрацию посещения отменён"

        private const val REGISTER_VISIT_INTRO =
            "%s\n\n" +
                    "Если хочешь отметиться на паре, пришли мне следующую информацию: " +
                    "дата посещения, номер на фото (каждый пункт с новой строки).\n\n" +
                    "*Например:*\n" +
                    "26.02.2023\n" +
                    "43\n\n" +
                    "Для отмены действия, отправь в чат сообщение с текстом \"$CANCEL_COMMAND\""
        private const val VISIT_REGISTER_SUCCEED = "Посещение получено!"
        private const val VISIT_REGISTER_ERROR =
            "Ошибка регистрации посещения. Проверьте правильность введённых данных!"
        private const val VISIT_ALREADY_EXIST = "Лицо с указанным номером уже зарегистрировано в этот день. " +
                "Попробуйте ввести верное значение.\n\n" +
                "Если вы уверены, что это вы, напишите @y2kot"

        private const val UNKNOWN_USER = "Увы, но кажется мы не знакомы \uD83D\uDE1E"
    }
}
