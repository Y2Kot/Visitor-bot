package ru.kudryavtsev.domain.util

import ru.kudryavtsev.domain.model.VisitParserException
import ru.kudryavtsev.domain.model.VisitPayload
import java.time.LocalDate
import java.time.format.DateTimeFormatter

fun String?.answerParser(): VisitPayload {
    val studentAnswer = this ?: throw VisitParserException.NullInputData

    val (dateStr, number) = try {
        studentAnswer.trim().split("\n")
    } catch (e: RuntimeException) {
        throw VisitParserException.InvalidInputData
    }

    val date = try {
        val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        LocalDate.parse(dateStr.trim(), formatter)
    } catch (e: RuntimeException) {
        throw VisitParserException.InvalidDateFormat
    }

    val numberValue = try {
        number.trim().toInt()
    } catch (e: RuntimeException) {
        throw VisitParserException.InvalidPhotoNumber
    }

    return VisitPayload(
        date = date,
        number = numberValue
    )
}
