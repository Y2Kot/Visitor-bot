package ru.kudryavtsev.domain.repository

import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import ru.kudryavtsev.datasource.local.mappers.toDomain
import ru.kudryavtsev.domain.model.Visit

class VisitRepository {
    fun registerVisit(visit: Visit) {
        transaction {
            addLogger(StdOutSqlLogger)
            ru.kudryavtsev.datasource.local.entity.VisitEntity.new {
                studentId = visit.studentId
                date = visit.date
                subject = visit.subject.ordinal
                numberOnImage = visit.numberOnImage
                isPublished = visit.isPublished
            }
        }
    }

    fun getAllVisits(): List<Visit> = transaction {
        addLogger(StdOutSqlLogger)
        ru.kudryavtsev.datasource.local.entity.VisitEntity.all().map { it.toDomain() }
    }
}