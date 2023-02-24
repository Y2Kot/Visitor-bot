package ru.kudryavtsev.domain.model

import java.util.UUID

data class Student(
    val id: UUID = UUID.randomUUID(),
    val userId: Long,
    val chatId: Long,
    val name: String,
    val group: Group
)