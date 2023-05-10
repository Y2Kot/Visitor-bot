package ru.kudryavtsev

import ru.kudryavtsev.model.Cell
import ru.kudryavtsev.model.Week

const val WEEKS_COUNT = 17
const val LESSONS_PER_WEEK = 4
const val SURNAME_SEARCH = "B5:B31"
const val STUDENTS_OFFSET = 5

private val cellIndexes = listOf(
    "C",
    "D",
    "E",
    "F",
    "G",
    "H",
    "I",
    "J",
    "K",
    "L",
    "M",
    "N",
    "O",
    "P",
    "Q",
    "R",
    "S",
    "T",
    "U",
    "V",
    "W",
    "X",
    "Y",
    "Z",
    "AA",
    "AB",
    "AC",
    "AD",
    "AE",
    "AF",
    "AG",
    "AH",
    "AI",
    "AJ",
    "AK",
    "AL",
    "AM",
    "AN",
    "AO",
    "AP",
    "AQ",
    "AR",
    "AS",
    "AT",
    "AU",
    "AV",
    "AW",
    "AX",
    "AY",
    "AZ",
    "BA",
    "BB",
    "BC",
    "BD",
    "BE",
    "BF",
    "BG",
    "BH",
    "BI",
    "BJ",
    "BK",
    "BL",
    "BM",
    "BN",
    "BO",
    "BP",
    "BQ",
    "BR",
)

val weeks: List<Week> = buildList {
    repeat(WEEKS_COUNT) { iteration ->
        val week = Week(
            number = iteration + 1,
            opLecture = Cell(column = cellIndexes[iteration * LESSONS_PER_WEEK + 0]),
            opLaboratory = Cell(column = cellIndexes[iteration * LESSONS_PER_WEEK + 1]),
            oopLecture = Cell(column = cellIndexes[iteration * LESSONS_PER_WEEK + 2]),
            oopLaboratory = Cell(column = cellIndexes[iteration * LESSONS_PER_WEEK + 3]),
        )
        add(week)
    }
}
