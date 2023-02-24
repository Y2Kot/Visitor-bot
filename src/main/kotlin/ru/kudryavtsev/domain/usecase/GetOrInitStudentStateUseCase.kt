package ru.kudryavtsev.domain.usecase

import ru.kudryavtsev.domain.model.StudentState
import ru.kudryavtsev.domain.service.StudentStateService

class GetOrInitStudentStateUseCase(private val service: StudentStateService) {
    operator fun get(key: Long): StudentState = service.getState(key) ?: run {
        service.setState(key,StudentState.Undefined)
        StudentState.Undefined
    }
}
