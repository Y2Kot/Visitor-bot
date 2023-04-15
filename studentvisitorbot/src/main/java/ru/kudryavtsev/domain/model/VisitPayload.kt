package ru.kudryavtsev.domain.model

import java.time.LocalDate

data class VisitPayload(
    val date: LocalDate,
    val number: Int
)
