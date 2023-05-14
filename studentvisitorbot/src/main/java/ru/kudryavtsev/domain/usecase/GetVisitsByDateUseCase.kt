package ru.kudryavtsev.domain.usecase

import ru.kudryavtsev.domain.repository.VisitRepository
import java.time.LocalDate

class GetVisitsByDateUseCase(private val visitRepository: VisitRepository) {
    operator fun invoke(date: LocalDate) = visitRepository.getVisitByDate(date)
}
