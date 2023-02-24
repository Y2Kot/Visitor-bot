package ru.kudryavtsev.datasource.local.services

import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import ru.kudryavtsev.datasource.local.entity.AdministratorEntity
import ru.kudryavtsev.datasource.local.entity.Administrators

class AdministratorDaoService {
    fun findByUserId(id: Long): AdministratorEntity? = transaction {
        addLogger(StdOutSqlLogger)
        AdministratorEntity
            .find { Administrators.userId eq id }
            .firstOrNull()
    }
}
