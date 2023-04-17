package ru.kudryavtsev.datasource.remote

import kotlinx.coroutines.flow.Flow
import ru.kudryavtsev.datasource.remote.model.MessageDto

class BotService(private val botApi: BotApi): IBotService {
    override fun getMessages(): Flow<MessageDto> = botApi.botMessages

    override fun sendMessage(message: MessageDto) {
        botApi.sendMessage(message)
    }
}
