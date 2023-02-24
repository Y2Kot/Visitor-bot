package ru.kudryavtsev.datasource.local.mappers

import ru.kudryavtsev.datasource.local.entity.StudentEntity
import ru.kudryavtsev.domain.model.Group
import ru.kudryavtsev.domain.model.Student

fun StudentEntity.toDomain(): Student = Student(
    id = id.value,
    userId = userId,
    chatId = chatId,
    name = name,
    group = Group.values()[group]
)
