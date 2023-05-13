package ru.kudryavtsev.model

/**
 * @param id - номер зачётной книжки или студенческого билета
 * @param name - ФИО студента, совпадающее с общим форматом системы
 * @param group - текстовое название группы, берётся из имени файла
 */
data class StudentRecord(
    val id: String,
    val name: String,
    val group: String
)
