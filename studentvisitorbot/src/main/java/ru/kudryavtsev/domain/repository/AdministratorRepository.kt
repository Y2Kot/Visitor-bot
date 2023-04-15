package ru.kudryavtsev.domain.repository

import ru.kudryavtsev.datasource.local.mappers.toDomain
import ru.kudryavtsev.datasource.local.services.AdministratorDaoService
import ru.kudryavtsev.domain.model.Administrator

class AdministratorRepository(private val service: AdministratorDaoService) {
    fun getAdministratorById(id: Long): Administrator? = service.findByUserId(id)?.toDomain()
}
