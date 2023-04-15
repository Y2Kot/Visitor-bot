package ru.kudryavtsev.domain.usecase

import kotlinx.coroutines.flow.Flow
import ru.kudryavtsev.model.Message
import ru.kudryavtsev.domain.repository.BotRepository

class ReceiveMessagesUseCase(private val repository: BotRepository) {
    operator fun invoke(): Flow<Message> = repository.receiveMessages()
}
