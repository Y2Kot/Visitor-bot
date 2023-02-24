package ru.kudryavtsev.datasource.local.mappers

import ru.kudryavtsev.datasource.local.entity.VisitEntity
import ru.kudryavtsev.domain.model.Subject
import ru.kudryavtsev.domain.model.Visit

fun VisitEntity.toDomain(): Visit = Visit(
    id = id.value,
    studentId = studentId,
    date = date,
    subject = Subject.values()[subject],
    numberOnImage = numberOnImage,
    isPublished = isPublished
)