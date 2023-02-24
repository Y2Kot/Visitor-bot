package ru.kudryavtsev.domain.usecase

import ru.kudryavtsev.domain.model.Message
import ru.kudryavtsev.domain.repository.BotRepository

class SendMessageUseCase(private val repository: BotRepository) {
    operator fun invoke(message: Message) {
        repository.sendMessage(message)
    }
}
