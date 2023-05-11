package ru.kudryavtsev

import ru.kudryavtsev.CsvLoader.readCsvNotNull
import ru.kudryavtsev.model.StudentRecord
import java.io.File

class ReadCsvUseCase {
    operator fun invoke(file: File): List<StudentRecord> =
        file.readCsvNotNull { tokenList ->
            if (tokenList.size < 2) {
                return@readCsvNotNull null
            }
            val (name, id) = tokenList
            StudentRecord(name, id)
        }
}
