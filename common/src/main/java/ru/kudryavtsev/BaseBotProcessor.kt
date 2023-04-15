package ru.kudryavtsev

import kotlinx.coroutines.flow.Flow
import ru.kudryavtsev.model.Message

interface BaseBotProcessor {
    val messages: Flow<Message>
}