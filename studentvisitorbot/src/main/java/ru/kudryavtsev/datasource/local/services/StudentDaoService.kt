package ru.kudryavtsev.datasource.local.services

import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import ru.kudryavtsev.datasource.local.entity.StudentEntity
import ru.kudryavtsev.datasource.local.entity.Students
import ru.kudryavtsev.domain.model.Student
import java.util.*

class StudentDaoService {
    fun findByUserId(id: UUID): StudentEntity? = transaction {
        addLogger(StdOutSqlLogger)
        StudentEntity
            .find { Students.id eq id }
            .firstOrNull()
    }
    fun findByTelegramId(id: Long): StudentEntity? = transaction {
        addLogger(StdOutSqlLogger)
        StudentEntity
            .find { Students.telegramId eq id }
            .firstOrNull()
    }

    fun insert(student: Student) = transaction {
        addLogger(StdOutSqlLogger)
        StudentEntity.new {
            telegramId = student.userId
            chatId = student.chatId
            name = student.name
            group = student.group
        }
    }

    fun readAll(): List<StudentEntity> = transaction {
        addLogger(StdOutSqlLogger)
        StudentEntity.all().toList()
    }
}
