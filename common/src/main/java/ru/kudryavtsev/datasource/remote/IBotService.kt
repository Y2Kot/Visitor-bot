package ru.kudryavtsev.datasource.remote

import kotlinx.coroutines.flow.Flow
import ru.kudryavtsev.datasource.remote.model.MessageDto

interface IBotService {
    fun getMessages(): Flow<MessageDto>
    fun sendMessage(message: MessageDto)
}
