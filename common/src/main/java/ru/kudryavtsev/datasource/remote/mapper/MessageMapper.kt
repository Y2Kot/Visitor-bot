package ru.kudryavtsev.datasource.remote.mapper

import ru.kudryavtsev.datasource.remote.model.MessageDto
import ru.kudryavtsev.datasource.remote.model.UserInfoDto
import ru.kudryavtsev.domain.model.Message
import ru.kudryavtsev.domain.model.UserInfo


fun MessageDto.toDomain(): Message = Message(
    messageId = messageId,
    userInfo = userInfo.toDomain(),
    replyId = replyId,
    text = text,
    attachment = attachment,
    isCommand = isCommand
)

fun Message.toDto(): MessageDto = MessageDto(
    messageId = messageId,
    userInfo = userInfo.toDto(),
    replyId = replyId,
    text = text,
    attachment = attachment,
    isCommand = isCommand
)

fun UserInfo.toDto(): UserInfoDto = UserInfoDto(
    userId = userId,
    chatId = chatId
)

fun UserInfoDto.toDomain(): UserInfo = UserInfo(
    userId = userId,
    chatId = chatId
)
