package ru.kudryavtsev.datasource.local.mappers

import ru.kudryavtsev.datasource.local.entity.VisitEntity
import ru.kudryavtsev.datasource.local.model.Subject as SubjectEntity
import ru.kudryavtsev.domain.model.Subject
import ru.kudryavtsev.domain.model.Visit

fun VisitEntity.toDomain(): Visit = Visit(
    id = id.value,
    studentId = studentId,
    date = date,
    subject = subject.toDomain(),
    numberOnImage = numberOnImage,
    isPublished = isPublished
)

fun SubjectEntity.toDomain(): Subject = when(this) {
    SubjectEntity.OP -> Subject.OP
    SubjectEntity.OOP -> Subject.OOP
}

fun Subject.toEntity(): SubjectEntity = when(this) {
    Subject.OP -> SubjectEntity.OP
    Subject.OOP -> SubjectEntity.OOP
}
