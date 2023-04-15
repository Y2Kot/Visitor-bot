package ru.kudryavtsev.datasource.local.entity

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import org.jetbrains.exposed.sql.javatime.date
import ru.kudryavtsev.datasource.local.model.Subject
import java.time.LocalDate
import java.util.UUID

object Visits : UUIDTable("visits") {
    val studentId = uuid("student_id")
    val date = date("date")
    val subject = enumeration<Subject>("subject")
    val numberOnImage = integer("number_on_image")
    val isPublished = bool("is_published")
}

class VisitEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<VisitEntity>(Visits)

    var studentId: UUID by Visits.studentId
    var date: LocalDate by Visits.date
    var subject: Subject by Visits.subject
    var numberOnImage: Int by Visits.numberOnImage
    var isPublished: Boolean by Visits.isPublished
}
