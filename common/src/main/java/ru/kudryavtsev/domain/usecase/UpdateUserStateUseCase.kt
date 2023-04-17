package ru.kudryavtsev.domain.usecase

import ru.kudryavtsev.domain.model.BaseUserState
import ru.kudryavtsev.domain.service.UserStateService

class UpdateUserStateUseCase(private val service: UserStateService) {
    operator fun set(key: Long, state: BaseUserState) {
        service.setState(key, state)
    }
}
