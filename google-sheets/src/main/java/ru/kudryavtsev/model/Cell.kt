package ru.kudryavtsev.model

data class Cell(
    val column: String? = null,
    val row: Int? = null,
    val data: String = ""
) {
    val cell = "$column$row"
}
