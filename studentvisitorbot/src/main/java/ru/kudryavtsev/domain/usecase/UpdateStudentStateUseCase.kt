package ru.kudryavtsev.domain.usecase

import ru.kudryavtsev.domain.model.StudentState
import ru.kudryavtsev.domain.service.StudentStateService

class UpdateStudentStateUseCase(private val service: StudentStateService) {
    operator fun set(key: Long, state: StudentState) {
        service.setState(key, state)
    }
}
