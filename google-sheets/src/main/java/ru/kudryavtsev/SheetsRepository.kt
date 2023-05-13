package ru.kudryavtsev

import ru.kudryavtsev.model.Discipline

import ru.kudryavtsev.model.StudentDescription

class SheetsRepository(private val service: SheetsService) {
    fun updateStudentByName(student: StudentDescription, weekNumber: Long, discipline: Discipline) {
        val studentRow = service.findStudentRow(student)
        service.updateStudentVisit(student.group, studentRow, weekNumber, discipline)
    }
}
