package ru.kudryavtsev.domain.repository

import ru.kudryavtsev.datasource.local.mappers.toDomain
import ru.kudryavtsev.datasource.local.services.VisitDaoService
import ru.kudryavtsev.domain.model.Visit
import java.time.LocalDate

class VisitRepository(private val service: VisitDaoService) {
    fun registerVisit(visit: Visit) {
        service.create(visit)
    }

    fun getVisitByDate(date: LocalDate): List<Visit> = service
        .readReadByDate(date)
        .map { it.toDomain() }
    fun getAllVisits(): List<Visit> = service
        .readAll()
        .map { it.toDomain() }
}
