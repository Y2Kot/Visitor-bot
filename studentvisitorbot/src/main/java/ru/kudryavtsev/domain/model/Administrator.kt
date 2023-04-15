package ru.kudryavtsev.domain.model

import java.util.UUID

data class Administrator(
    val id: UUID,
    val isEnabled: Boolean
)
