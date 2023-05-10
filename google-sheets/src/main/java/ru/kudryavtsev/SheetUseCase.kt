package ru.kudryavtsev

import ru.kudryavtsev.model.Discipline
import ru.kudryavtsev.model.StudentDescription

class SheetUseCase(private val repository: SheetRepository) {
    operator fun invoke(student: StudentDescription, weekNumber: Int, discipline: Discipline) {
        repository.updateStudentByName(student, weekNumber, discipline)
    }
}
