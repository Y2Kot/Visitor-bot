package ru.kudryavtsev.domain

import kotlinx.coroutines.flow.Flow
import ru.kudryavtsev.domain.model.Message

interface BaseBotProcessor {
    val messages: Flow<Message>
}
