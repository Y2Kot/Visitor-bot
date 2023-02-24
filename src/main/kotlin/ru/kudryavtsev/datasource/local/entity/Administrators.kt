package ru.kudryavtsev.datasource.local.entity

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.UUID

object Administrators : UUIDTable("administrators") {
    val userId = long("telegram_id").uniqueIndex()
    val isEnabled = bool("is_enabled")
}

class AdministratorEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<AdministratorEntity>(Administrators)

    var userId: Long by Administrators.userId
    var isEnabled: Boolean by Administrators.isEnabled
}
