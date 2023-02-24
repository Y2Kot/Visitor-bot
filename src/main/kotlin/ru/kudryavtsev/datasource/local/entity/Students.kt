package ru.kudryavtsev.datasource.local.entity

import org.jetbrains.exposed.dao.UUIDEntity
import org.jetbrains.exposed.dao.UUIDEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.UUIDTable
import java.util.UUID

object Students : UUIDTable("students") {
    val userId = long("telegram_id").uniqueIndex()
    val chatId = long("chat_id").uniqueIndex()
    val name = varchar("name", 50)
    val group = integer("group")
}

class StudentEntity(id: EntityID<UUID>) : UUIDEntity(id) {
    companion object : UUIDEntityClass<StudentEntity>(Students)

    var userId: Long by Students.userId
    var chatId: Long by Students.chatId
    var name: String by Students.name
    var group: Int by Students.group
}
