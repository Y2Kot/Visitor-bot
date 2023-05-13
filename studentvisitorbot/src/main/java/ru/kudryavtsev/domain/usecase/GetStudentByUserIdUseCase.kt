package ru.kudryavtsev.domain.usecase

import ru.kudryavtsev.domain.model.Student
import ru.kudryavtsev.domain.repository.StudentsRepository
import java.util.UUID

class GetStudentByUserIdUseCase(private val repository: StudentsRepository) {
    operator fun get(id: UUID): Student? = repository.getByUserId(id)
}
