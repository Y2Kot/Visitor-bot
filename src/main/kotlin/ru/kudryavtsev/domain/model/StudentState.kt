package ru.kudryavtsev.domain.model

sealed interface StudentState {
    object Undefined: StudentState
    object Registered: StudentState
    data class Registering(
        val registeringStep: RegisteringStep = RegisteringStep.First,
        val student: Student
    ): StudentState
    data class AddingVisit(val discipline: Discipline): StudentState
    object UploadingImage: StudentState
}

sealed interface RegisteringStep {
    object First: RegisteringStep
    object Second: RegisteringStep
}

sealed interface Discipline {
    object Op: Discipline
    object Oop: Discipline
}
