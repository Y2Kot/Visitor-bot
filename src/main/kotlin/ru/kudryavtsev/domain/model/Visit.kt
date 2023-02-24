package ru.kudryavtsev.domain.model

import java.time.LocalDate
import java.util.UUID

data class Visit(
    val id: UUID = UUID.randomUUID(),
    val studentId: UUID,
    val date: LocalDate,
    val subject: Subject,
    val numberOnImage: Int,
    val isPublished: Boolean = false
)