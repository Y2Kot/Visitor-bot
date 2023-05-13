package ru.kudryavtsev.domain.util

import ru.kudryavtsev.SEMESTER_START_DATE
import java.time.LocalDate
import java.time.temporal.ChronoUnit
import kotlin.math.abs

fun LocalDate.currentStudyingWeek(): Long {
   val diff = ChronoUnit.DAYS.between(SEMESTER_START_DATE, this) / 7 + 1
   return abs(diff)
}
