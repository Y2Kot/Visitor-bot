package ru.kudryavtsev.domain.repository

import ru.kudryavtsev.datasource.local.mappers.toDomain
import ru.kudryavtsev.datasource.local.services.StudentDaoService
import ru.kudryavtsev.domain.model.Student

class StudentsRepository(private val service: StudentDaoService) {
    fun getUserById(id: Long): Student? = service.findByUserId(id)?.toDomain()

    fun registerStudent(student: Student) {
        service.insert(student)
    }

    fun getAllStudents(): List<Student> = service.readAll().map { it.toDomain() }
}
