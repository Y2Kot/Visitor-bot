package ru.kudryavtsev.domain.usecase

import ru.kudryavtsev.domain.repository.AdministratorRepository

class CheckAdminPermissionUseCase(private val repository: AdministratorRepository) {
    operator fun invoke(id: Long): Boolean = repository.getAdministratorById(id) != null
}
