package ru.kudryavtsev.datasource.local.services

import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import ru.kudryavtsev.datasource.local.entity.VisitEntity
import ru.kudryavtsev.datasource.local.mappers.toEntity
import ru.kudryavtsev.domain.model.Visit

class VisitDaoService {
    fun readAll(): List<VisitEntity> = transaction {
        addLogger(StdOutSqlLogger)
        VisitEntity.all().toList()
    }

    fun create(visit: Visit) {
        transaction {
            addLogger(StdOutSqlLogger)
            VisitEntity.new {
                studentId = visit.studentId
                date = visit.date
                subject = visit.subject.toEntity()
                numberOnImage = visit.numberOnImage
                isPublished = visit.isPublished
            }
        }
    }
}
