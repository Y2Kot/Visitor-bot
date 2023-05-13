package ru.kudryavtsev

import ru.kudryavtsev.CsvLoader.readCsvNotNull
import ru.kudryavtsev.model.StudentRecord
import java.io.File

class ReadCsvUseCase {
    operator fun invoke(registry: File): List<StudentRecord> {
        val students = registry.listFiles()?.map { file ->
            file.readCsvNotNull { tokenList ->
                if (tokenList.size < 2) {
                    return@readCsvNotNull null
                }
                val (name, id) = tokenList
                StudentRecord(
                    id = id,
                    name = name,
                    group = file.nameWithoutExtension
                )
            }
        }?.flatten()
        return students ?: error("Student registry not found")
    }
}
