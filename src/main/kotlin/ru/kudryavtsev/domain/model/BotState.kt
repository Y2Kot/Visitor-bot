package ru.kudryavtsev.domain.model

sealed interface BotState {
    object Undefined: BotState
    object Registered: BotState
    data class Registering(
        val registeringStep: RegisteringStep = RegisteringStep.First,
        val student: Student
    ): BotState
    data class AddingVisit(val discipline: Discipline): BotState
    object UploadingImage: BotState
}

sealed interface RegisteringStep {
    object First: RegisteringStep
    object Second: RegisteringStep
}

sealed interface Discipline {
    object Op: Discipline
    object Oop: Discipline
}