package ru.kudryavtsev.domain.usecase

import ru.kudryavtsev.domain.model.Student
import ru.kudryavtsev.domain.repository.StudentsRepository

class GetAllStudentsUseCase(private val repository: StudentsRepository) {
    operator fun invoke(): List<Student> = repository.getAllStudents()
}
