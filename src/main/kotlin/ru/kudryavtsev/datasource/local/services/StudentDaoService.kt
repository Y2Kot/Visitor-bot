package ru.kudryavtsev.datasource.local.services

import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import ru.kudryavtsev.datasource.local.entity.StudentEntity
import ru.kudryavtsev.datasource.local.entity.Students
import ru.kudryavtsev.domain.model.Student

class StudentDaoService {
    fun findByUserId(id: Long): StudentEntity? = transaction {
        addLogger(StdOutSqlLogger)
        StudentEntity
            .find { Students.userId eq id }
            .firstOrNull()
    }

    fun insert(student: Student) = transaction {
        addLogger(StdOutSqlLogger)
        StudentEntity.new {
            userId = student.userId
            chatId = student.chatId
            name = student.name
            group = student.group.ordinal
        }
    }

    fun readAll(): List<StudentEntity> = transaction {
        addLogger(StdOutSqlLogger)
        StudentEntity.all().toList()
    }
}
