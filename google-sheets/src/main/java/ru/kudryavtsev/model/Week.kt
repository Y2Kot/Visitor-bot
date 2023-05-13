package ru.kudryavtsev.model

// TODO Must be refactored in free time. For now just make it work
data class Week(
    val number: Long,
    val opLecture: Cell,
    val opLaboratory: Cell,
    val oopLecture: Cell,
    val oopLaboratory: Cell,
)
