package ru.kudryavtsev.datasource.local.mappers

import ru.kudryavtsev.datasource.local.entity.StudentEntity
import ru.kudryavtsev.domain.model.Student

fun StudentEntity.toDomain(): Student = Student(
    id = id.value,
    userId = telegramId,
    chatId = chatId,
    name = name,
    group = group
)
