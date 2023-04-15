package ru.kudryavtsev.datasource.remote.mappers

import ru.kudryavtsev.datasource.remote.model.MessageDto
import ru.kudryavtsev.datasource.remote.model.UserInfoDto
import ru.kudryavtsev.model.Message
import ru.kudryavtsev.model.UserInfo


fun MessageDto.toDomain(): Message = Message(
    messageId = messageId,
    userInfo = userInfo.toDomain(),
    replyId = replyId,
    text = text,
    isCommand = isCommand
)

fun Message.toDto(): MessageDto = MessageDto(
    messageId = messageId,
    userInfo = userInfo.toDto(),
    replyId = replyId,
    text = text,
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