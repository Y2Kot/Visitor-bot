package ru.kudryavtsev.domain.repository

import org.jetbrains.exposed.sql.StdOutSqlLogger
import org.jetbrains.exposed.sql.addLogger
import org.jetbrains.exposed.sql.transactions.transaction
import ru.kudryavtsev.datasource.local.entity.StudentEntity
import ru.kudryavtsev.datasource.local.entity.Students
import ru.kudryavtsev.datasource.local.mappers.toDomain
import ru.kudryavtsev.domain.model.Student

class StudentsRepository {
    fun isStudentExist(id: Long): Boolean = getUserById(id) != null

    fun getUserById(id: Long): Student? = transaction {
        addLogger(StdOutSqlLogger)
        StudentEntity.find { Students.userId eq id }
            .map { it.toDomain() }
            .firstOrNull()
    }

    fun registerStudent(student: Student) {
        transaction {
            addLogger(StdOutSqlLogger)
            StudentEntity.new {
                userId = student.userId
                chatId = student.chatId
                name = student.name
                group = student.group.ordinal
            }
        }
    }

    fun getAllStudents(): List<Student> = transaction {
        addLogger(StdOutSqlLogger)
        StudentEntity.all().map { it.toDomain() }
    }
}
