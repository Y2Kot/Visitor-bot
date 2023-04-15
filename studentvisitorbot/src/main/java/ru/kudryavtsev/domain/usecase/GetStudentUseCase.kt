package ru.kudryavtsev.domain.usecase

import ru.kudryavtsev.domain.model.Student
import ru.kudryavtsev.domain.repository.StudentsRepository

class GetStudentUseCase(private val repository: StudentsRepository) {
    operator fun get(id: Long): Student? = repository.getUserById(id)
}
