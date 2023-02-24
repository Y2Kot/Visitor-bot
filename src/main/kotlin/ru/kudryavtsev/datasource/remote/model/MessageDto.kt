package ru.kudryavtsev.datasource.remote.model

import java.io.File


data class MessageDto(
    val userInfo: UserInfoDto,
    val messageId: Int,
    val replyId: Int? = null,
    val text: String?,
    val attachment: File? = null,
    val isCommand: Boolean
)
