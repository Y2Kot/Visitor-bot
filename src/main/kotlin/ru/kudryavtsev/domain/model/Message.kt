package ru.kudryavtsev.domain.model

import java.io.File


data class Message(
    val messageId: Int = -1,
    val userInfo: UserInfo,
    val replyId: Int? = null,
    val text: String?,
    val attachment: File? = null,
    val isCommand: Boolean = false
)
