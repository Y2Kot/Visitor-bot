package ru.kudryavtsev.domain.usecase

import ru.kudryavtsev.domain.model.Student
import ru.kudryavtsev.domain.repository.StudentsRepository

class RegisterStudentUseCase(private val repository: StudentsRepository) {
    operator fun invoke(student: Student) {
        repository.registerStudent(student)
    }
}
