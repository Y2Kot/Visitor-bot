package ru.kudryavtsev.domain.usecase

import ru.kudryavtsev.domain.model.Visit
import ru.kudryavtsev.domain.repository.VisitRepository

class RegisterVisitUseCase(private val repository: VisitRepository) {
    operator fun invoke(visit: Visit) {
        repository.registerVisit(visit)
    }
}
