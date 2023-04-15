package ru.kudryavtsev.datasource.remote

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.TelegramBotsApi
import org.telegram.telegrambots.meta.api.methods.GetFile
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession
import ru.kudryavtsev.datasource.remote.model.MessageDto
import ru.kudryavtsev.datasource.remote.model.UserInfoDto
import ru.kudryavtsev.AppContext
import java.io.File


class BotApi(context: AppContext) : TelegramLongPollingBot(context.token) {
    private val _botMessages = MutableSharedFlow<MessageDto>(
        extraBufferCapacity = 100,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val botMessages: SharedFlow<MessageDto> = _botMessages

    init {
        val botsApi = TelegramBotsApi(DefaultBotSession::class.java)
        botsApi.registerBot(this)
    }

    override fun getBotUsername(): String = BOT_NAME

    override fun onUpdateReceived(update: Update?) {
        update ?: return
        val message = update.message
        val attachment = if (message.hasDocument()) {
            update.downloadFile()
        } else {
            null
        }
        val data = MessageDto(
            messageId = message.messageId,
            userInfo = UserInfoDto(
                userId = message.from.id,
                chatId = message.chatId,
            ),
            text = message.text ?: message.caption,
            attachment = attachment,
            isCommand = message.isCommand
        )
        _botMessages.tryEmit(data)
    }

    fun sendMessage(message: MessageDto) {
        val newMessage = SendMessage.builder()
            .chatId(message.userInfo.chatId)
            .replyToMessageId(message.replyId)
            .parseMode("markdown")
            .text(message.text ?: "") // TODO make better
        execute(newMessage.build())
    }

    private fun Update.downloadFile(): File {
        val getFile = GetFile(message.document.fileId)
        val fileName = message.document.fileName
        val path = execute(getFile).filePath

        val projectPath = System.getProperty("user.dir")
        val file = File(projectPath, fileName)
        downloadFile(path, file)
        return file
    }

    private companion object {
        private const val BOT_NAME = "Посещаемость СГН3 ОП/ООП"
    }
}
