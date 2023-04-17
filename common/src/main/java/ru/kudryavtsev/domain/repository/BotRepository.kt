package ru.kudryavtsev.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kudryavtsev.datasource.remote.IBotService
import ru.kudryavtsev.datasource.remote.mapper.toDomain
import ru.kudryavtsev.datasource.remote.mapper.toDto
import ru.kudryavtsev.domain.model.Message

class BotRepository(private val service: IBotService) {
    fun sendMessage(message: Message) {
        service.sendMessage(message.toDto())
    }

    fun receiveMessages(): Flow<Message> = service.getMessages().map { it.toDomain() }
}
