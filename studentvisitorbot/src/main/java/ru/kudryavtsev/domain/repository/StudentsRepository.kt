package ru.kudryavtsev.domain.repository

import ru.kudryavtsev.datasource.local.mappers.toDomain
import ru.kudryavtsev.datasource.local.services.StudentDaoService
import ru.kudryavtsev.domain.model.Student
import java.util.UUID

class StudentsRepository(private val service: StudentDaoService) {
    fun getByTelegramId(id: Long): Student? = service.findByTelegramId(id)?.toDomain()

    fun getByUserId(id: UUID): Student? = service.findByUserId(id)?.toDomain()

    fun registerStudent(student: Student) {
        service.insert(student)
    }

    fun getAllStudents(): List<Student> = service.readAll().map { it.toDomain() }
}
