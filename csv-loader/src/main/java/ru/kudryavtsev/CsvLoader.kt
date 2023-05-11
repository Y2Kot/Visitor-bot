package ru.kudryavtsev

import java.io.File

object CsvLoader {
    fun File.readCsv(separator: Char = ';'): List<List<String>> =
        this.useLines { lines ->
            lines.map { line ->
                line.split(separator)
            }.toList()
        }

    fun <R> File.readCsv(separator: Char = ';', transform: (List<String>) -> R): List<R> =
        this.useLines { lines ->
            lines
                .map { line -> line.split(separator) }
                .map(transform)
                .toList()
        }

    fun <R> File.readCsvNotNull(separator: Char = ';', transform: (List<String>) -> R?): List<R> =
        this.useLines { lines ->
            lines
                .map { line -> line.split(separator) }
                .mapNotNull(transform)
                .toList()
        }
}
