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
    SubjectEntity.OP_LECTURE -> Subject.OP_LECTURE
    SubjectEntity.OP_LAB -> Subject.OP_LAB
    SubjectEntity.OOP_LECTURE -> Subject.OOP_LECTURE
    SubjectEntity.OOP_LAB -> Subject.OOP_LAB
}

fun Subject.toEntity(): SubjectEntity = when(this) {
    Subject.OP_LECTURE -> SubjectEntity.OP_LECTURE
    Subject.OP_LAB -> SubjectEntity.OP_LAB
    Subject.OOP_LECTURE -> SubjectEntity.OOP_LECTURE
    Subject.OOP_LAB -> SubjectEntity.OOP_LAB
}
