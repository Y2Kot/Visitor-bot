package ru.kudryavtsev.domain.di

import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module
import ru.kudryavtsev.ReadCsvUseCase

val csvModule = module {
    factoryOf(::ReadCsvUseCase)
}
