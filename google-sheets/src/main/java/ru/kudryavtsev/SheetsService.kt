package ru.kudryavtsev

import ru.kudryavtsev.model.Discipline
import ru.kudryavtsev.model.Group
import ru.kudryavtsev.model.Range
import ru.kudryavtsev.model.StudentDescription

class SheetsService(
    private val api: SheetsApi
) {
    fun findStudentRow(student: StudentDescription): Int {
        val range = Range(student.group.page + SURNAME_SEARCH)
        val studentIndex = api.readLines(range).flatten().indexOfFirst {
            it is String && it.contains(student.name)
        }
        return if (studentIndex == -1) {
            throw IndexOutOfBoundsException("Student not found")
        } else {
            studentIndex + STUDENTS_OFFSET
        }
    }

    fun updateStudentVisit(group: Group, rowIndex: Int, weekNumber: Int, discipline: Discipline) {
        val currentWeek = weeks.firstOrNull { it.number == weekNumber }
            ?: throw IndexOutOfBoundsException("Unknown week number")
        val cell = when (discipline) {
            Discipline.OP_LECTURE -> currentWeek.opLecture.copy(row = rowIndex)
            Discipline.OP_LABORATORY -> currentWeek.opLaboratory.copy(row = rowIndex)
            Discipline.OOP_LECTURE -> currentWeek.oopLecture.copy(row = rowIndex)
            Discipline.OOP_LABORATORY -> currentWeek.oopLaboratory.copy(row = rowIndex)
        }
        val range = Range("${group.page}${cell.cell}")
        api.updateValues(range, listOf(listOf(cell.data)))
    }
}
