package ru.kudryavtsev.datasource.local.mappers

import ru.kudryavtsev.datasource.local.entity.AdministratorEntity
import ru.kudryavtsev.domain.model.Administrator

fun AdministratorEntity.toDomain(): Administrator = Administrator(
    id = id.value,
    isEnabled = isEnabled
)
