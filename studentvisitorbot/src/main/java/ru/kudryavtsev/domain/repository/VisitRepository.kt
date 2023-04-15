package ru.kudryavtsev.domain.repository

import ru.kudryavtsev.datasource.local.mappers.toDomain
import ru.kudryavtsev.datasource.local.services.VisitDaoService
import ru.kudryavtsev.domain.model.Visit

class VisitRepository(private val service: VisitDaoService) {
    fun registerVisit(visit: Visit) {
        service.create(visit)
    }

    fun getAllVisits(): List<Visit> = service.readAll().map { it.toDomain() }
}
