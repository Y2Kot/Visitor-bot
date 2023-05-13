package ru.kudryavtsev.domain.interactor

import ru.kudryavtsev.SheetUseCase
import ru.kudryavtsev.domain.model.Subject
import ru.kudryavtsev.domain.model.UserNotFoundException
import ru.kudryavtsev.domain.model.Visit
import ru.kudryavtsev.domain.usecase.GetStudentByUserIdUseCase
import ru.kudryavtsev.domain.usecase.RegisterVisitUseCase
import ru.kudryavtsev.domain.util.currentStudyingWeek
import ru.kudryavtsev.model.Discipline
import ru.kudryavtsev.model.Group
import ru.kudryavtsev.model.StudentDescription

class RegisterVisitInteractor(
    private val registerVisitUseCase: RegisterVisitUseCase,
    private val getStudentByUserId: GetStudentByUserIdUseCase,
    private val sheet: SheetUseCase
) {
    operator fun invoke(visit: Visit) {
        registerVisitUseCase(visit)
        val student = getStudentByUserId[visit.studentId] ?: throw UserNotFoundException
        val studentDescription = StudentDescription(
            name = student.name,
            group = Group(student.group),
        )
        sheet(
            student = studentDescription,
            weekNumber = visit.date.currentStudyingWeek(),
            discipline = visit.subject.toDiscipline()
        )
    }

    private fun Subject.toDiscipline(): Discipline = when(this) {
        Subject.OP_LECTURE -> Discipline.OP_LECTURE
        Subject.OP_LAB -> Discipline.OP_LABORATORY
        Subject.OOP_LECTURE -> Discipline.OOP_LECTURE
        Subject.OOP_LAB -> Discipline.OOP_LABORATORY
    }
}
