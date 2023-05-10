package ru.kudryavtsev.domain.usecase

import ru.kudryavtsev.domain.model.BaseUserState
import ru.kudryavtsev.domain.service.UserStateService

class GetOrInitUserStateUseCase(private val service: UserStateService) {
    operator fun get(key: Long): BaseUserState = service.getState(key) ?: run {
        service.setState(key,BaseUserState.Undefined)
        BaseUserState.Undefined
    }
}
