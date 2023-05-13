package ru.kudryavtsev.domain.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import ru.kudryavtsev.SheetUseCase
import ru.kudryavtsev.SheetsApi
import ru.kudryavtsev.SheetsRepository
import ru.kudryavtsev.SheetsService
import ru.kudryavtsev.model.Spreadsheet

val googleSheetsModule = module {
    factory {
        val spreadsheet = Spreadsheet("1rEO_46F95V8bPRIyWuvuSDbfvirl3eDd9lDcstvQnak")
        SheetsApi(spreadsheet)
    }

    factoryOf(::SheetsService)
    factoryOf(::SheetsRepository)
    factoryOf(::SheetUseCase)
}
