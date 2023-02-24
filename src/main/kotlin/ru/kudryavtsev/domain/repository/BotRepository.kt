package ru.kudryavtsev.domain.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.kudryavtsev.datasource.remote.BotService
import ru.kudryavtsev.datasource.remote.mappers.toDomain
import ru.kudryavtsev.datasource.remote.mappers.toDto
import ru.kudryavtsev.domain.model.Message

class BotRepository(private val service: BotService) {
    fun sendMessage(message: Message) {
        service.sendMessage(message.toDto())
    }

    fun receiveMessages(): Flow<Message> = service.getMessages().map { it.toDomain() }
}