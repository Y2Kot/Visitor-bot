package ru.kudryavtsev.domain.model

private const val EXCEPTION_MESSAGE = "User not found"

object UserNotFoundException : RuntimeException(EXCEPTION_MESSAGE)
