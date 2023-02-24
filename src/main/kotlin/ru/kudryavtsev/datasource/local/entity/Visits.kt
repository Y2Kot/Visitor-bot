package ru.kudryavtsev.datasource.local.entity

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.date
import java.time.LocalDate
import java.util.UUID

object Visits: UUIDTable("visits") {
    val studentId = uuid("student_id")
    val date = date("date")
    val subject = integer("subject")
    val numberOnImage = integer("number_on_image")
    val isPublished = bool("is_published")
}

class VisitEntity(id: EntityID<UUID>): UUIDEntity(id) {
    companion object: UUIDEntityClass<ru.kudryavtsev.datasource.local.entity.VisitEntity>(ru.kudryavtsev.datasource.local.entity.Visits)
    var studentId: UUID by ru.kudryavtsev.datasource.local.entity.Visits.studentId
    var date: LocalDate by ru.kudryavtsev.datasource.local.entity.Visits.date
    var subject: Int by ru.kudryavtsev.datasource.local.entity.Visits.subject
    var numberOnImage: Int by ru.kudryavtsev.datasource.local.entity.Visits.numberOnImage
    var isPublished: Boolean by ru.kudryavtsev.datasource.local.entity.Visits.isPublished
}