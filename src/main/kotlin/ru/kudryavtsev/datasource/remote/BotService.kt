package ru.kudryavtsev.datasource.remote

import kotlinx.coroutines.flow.Flow
import ru.kudryavtsev.datasource.remote.model.MessageDto

class BotService(private val botApi: BotApi) {
    fun getMessages(): Flow<MessageDto> = botApi.botMessages

    fun sendMessage(message: MessageDto) {
        botApi.sendMessage(message)
    }
}