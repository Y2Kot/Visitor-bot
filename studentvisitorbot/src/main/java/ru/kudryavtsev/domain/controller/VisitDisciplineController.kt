package ru.kudryavtsev.domain.controller

import com.qoollo.logger.logw
import ru.kudryavtsev.domain.model.BaseUserState
import ru.kudryavtsev.domain.model.Discipline
import ru.kudryavtsev.domain.model.Message
import ru.kudryavtsev.domain.model.UserState
import ru.kudryavtsev.domain.model.Subject
import ru.kudryavtsev.domain.model.Visit
import ru.kudryavtsev.domain.usecase.GetStudentUseCase
import ru.kudryavtsev.domain.usecase.RegisterVisitUseCase
import ru.kudryavtsev.domain.usecase.SendMessageUseCase
import ru.kudryavtsev.domain.usecase.UpdateUserStateUseCase
import ru.kudryavtsev.domain.util.answerParser

class VisitDisciplineController(
    private val updateStudentState: UpdateUserStateUseCase,
    private val sendMessage: SendMessageUseCase,
    private val getStudent: GetStudentUseCase,
    private val registerVisit: RegisterVisitUseCase,
) {
    fun visitOp(message: Message) {
        updateStudentState[message.userInfo.userId] = UserState.AddingVisit(Discipline.Op)
        sendMessage(message.copy(text = REGISTER_VISIT_INTRO))
    }

    fun visitOop(message: Message) {
        updateStudentState[message.userInfo.userId] = UserState.AddingVisit(Discipline.Oop)
        sendMessage(message.copy(text = REGISTER_VISIT_INTRO))
    }

    fun process(message: Message, discipline: Discipline) {
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
        updateStudentState[message.userInfo.userId] = BaseUserState.Registered
    }


    companion object {
        private const val REGISTER_VISIT_INTRO =
            "Если хочешь отметиться на паре, пришли мне следующую информацию: " +
                    "дата посещения, номер на фото (каждый пункт с новой строки).\n\n" +
                    "*Например:*\n" +
                    "26.02.2022\n" +
                    "43"

        private const val VISIT_REGISTER_SUCCEED = "Посещение получено!"
        private const val UNKNOWN_USER = "Увы, но кажется мы не знакомы \uD83D\uDE1E"
    }
}
