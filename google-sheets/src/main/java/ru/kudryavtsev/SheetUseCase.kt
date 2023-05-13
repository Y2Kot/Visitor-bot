package ru.kudryavtsev

import ru.kudryavtsev.model.Discipline
import ru.kudryavtsev.model.StudentDescription

class SheetUseCase(private val repository: SheetsRepository) {
    operator fun invoke(student: StudentDescription, weekNumber: Long, discipline: Discipline) {
        repository.updateStudentByName(student, weekNumber, discipline)
    }
}
