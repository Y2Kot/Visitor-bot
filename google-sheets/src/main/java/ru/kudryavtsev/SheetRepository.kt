package ru.kudryavtsev

import ru.kudryavtsev.model.Discipline

import ru.kudryavtsev.model.StudentDescription

class SheetRepository(private val service: SheetsService) {
    fun updateStudentByName(student: StudentDescription, weekNumber: Int, discipline: Discipline) {
        val studentRow = service.findStudentRow(student)
        service.updateStudentVisit(student.group, studentRow, weekNumber, discipline)
    }
}
