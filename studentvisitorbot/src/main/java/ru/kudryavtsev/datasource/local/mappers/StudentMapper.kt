package ru.kudryavtsev.datasource.local.mappers

import ru.kudryavtsev.datasource.local.entity.StudentEntity
import ru.kudryavtsev.domain.model.Group
import ru.kudryavtsev.domain.model.Student
import ru.kudryavtsev.datasource.local.model.Group as GroupEntity
fun StudentEntity.toDomain(): Student = Student(
    id = id.value,
    userId = userId,
    chatId = chatId,
    name = name,
    group = group.toDomain()
)

fun GroupEntity.toDomain(): Group = when(this) {
    GroupEntity.SGN_1 -> Group.SGN_1
    GroupEntity.SGN_2 -> Group.SGN_2
    GroupEntity.SGN_3 -> Group.SGN_3
    GroupEntity.SGN_4 -> Group.SGN_4
    GroupEntity.UNDEFINED -> Group.UNDEFINED
}

fun Group.toEntity(): GroupEntity = when(this) {
    Group.SGN_1 -> GroupEntity.SGN_1
    Group.SGN_2 -> GroupEntity.SGN_2
    Group.SGN_3 -> GroupEntity.SGN_3
    Group.SGN_4 -> GroupEntity.SGN_4
    Group.UNDEFINED -> GroupEntity.UNDEFINED
}
