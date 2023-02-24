package ru.kudryavtsev.domain.repository

import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import ru.kudryavtsev.datasource.local.entity.AdministratorEntity
import ru.kudryavtsev.datasource.local.entity.Administrators
import ru.kudryavtsev.datasource.local.mappers.toDomain
import ru.kudryavtsev.domain.model.Administrator

class AdministratorRepository {
    fun getAdministratorById(id: Long): Administrator? = transaction {
        addLogger(StdOutSqlLogger)
        AdministratorEntity.find { Administrators.userId eq id }
            .map { it.toDomain() }
            .firstOrNull()
    }

    fun isAdministrator(id: Long): Boolean = getAdministratorById(id)?.isEnabled == true
}
