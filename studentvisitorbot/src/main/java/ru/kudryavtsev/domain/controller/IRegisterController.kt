package ru.kudryavtsev.domain.controller

import ru.kudryavtsev.domain.model.Message
import ru.kudryavtsev.domain.model.UserState

interface IRegisterController {
    fun registerUser(message: Message)
    fun processRegistration(message: Message, registerState: UserState.Registering)
    fun sendDummyMessage(message: Message)

    companion object {
        const val DUMMY_MESSAGE = "Для работы с ботом воспользуйтесь меню команд."
        const val STUDENT_ALREADY_EXIST = "Мы уже знакомы ❤️\n" +
                "Если ошиблись при написании регистрационных данных - обратитесь к лектору - @Y2Kot. " +
                "Ручное редактирование пока не доступно."
        const val REGISTER_SUCCESS = "Пользователь успешно зарегистрирован."
        const val REGISTER_FAILED = "При проверке данных произошла ошибка! Проверьте правильность вводимых данных."
    }
}
