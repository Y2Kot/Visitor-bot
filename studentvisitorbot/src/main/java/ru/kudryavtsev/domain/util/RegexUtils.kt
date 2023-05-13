package ru.kudryavtsev.domain.util

fun String.find(regex: Regex): String? = regex.find(this)?.value
