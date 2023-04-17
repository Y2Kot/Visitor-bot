package ru.kudryavtsev.domain.model

interface BaseUserState {
    object Undefined: BaseUserState
    object Registered: BaseUserState
}
