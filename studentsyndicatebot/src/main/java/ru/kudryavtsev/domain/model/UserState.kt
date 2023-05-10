package ru.kudryavtsev.domain.model

sealed interface UserState: BaseUserState {
    object UploadingImage: UserState
}
