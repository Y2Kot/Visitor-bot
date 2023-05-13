package ru.kudryavtsev.datasource.local.entity

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.*

object Students : UUIDTable("students") {
    val telegramId = long("telegram_id").uniqueIndex()
    val chatId = long("chat_id").uniqueIndex()
    val name = varchar("name", 50)
    val group = varchar("group", 50)
}

class StudentEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<StudentEntity>(Students)

    var telegramId: Long by Students.telegramId
    var chatId: Long by Students.chatId
    var name: String by Students.name
    var group: String by Students.group
}
