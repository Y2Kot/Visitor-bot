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

sealed class Discipline(val description: String) {
    object OpLecture: Discipline("*Дисциплина:* ОП\n*Тип занятия:* Лекция")
    object OpLab: Discipline("*Дисциплина:* ОП\n*Тип занятия:* Лабораторная")
    object OopLecture: Discipline("*Дисциплина:* ООП\n*Тип занятия:* Лекция")
    object OopLab: Discipline("*Дисциплина:* ООП\n*Тип занятия:* Лабораторная")
}
