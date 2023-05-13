package ru.kudryavtsev.model

@JvmInline
value class Group(val name: String) {
    val page: String get() = "$name!"
}
