package ru.kudryavtsev.domain.model

sealed interface UserState: BaseUserState {
    data class Registering(
        val registeringStep: RegisteringStep = RegisteringStep.First,
        val student: Student
    ): UserState
    data class AddingVisit(val discipline: Discipline): UserState
    object UploadingImage: UserState
}

sealed interface RegisteringStep {
    object First: RegisteringStep
    object Second: RegisteringStep
}

sealed interface Discipline {
    object Op: Discipline
    object Oop: Discipline
}
