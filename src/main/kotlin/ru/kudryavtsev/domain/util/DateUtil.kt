package ru.kudryavtsev.domain.util

import ru.kudryavtsev.domain.model.StudyWeek
import java.time.LocalDate
import java.time.temporal.ChronoUnit

operator fun StudyWeek.contains(localDate: LocalDate): Boolean {
    val isBorderDate = from.isEqual(localDate) || to.isEqual(localDate)
    val isBetweenDate = from.isBefore(localDate) && to.isAfter(localDate)
    return isBorderDate || isBetweenDate
}

fun LocalDate.weeksBetween(d2: LocalDate): Long {
    val daysBetween = ChronoUnit.DAYS.between(this, d2)
    val weeksCount = daysBetween / 7
    val weekModule = daysBetween % 7
    val lastWeek = if (weekModule > 4) 1 else 0
    return weeksCount + lastWeek
}
