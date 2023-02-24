package ru.kudryavtsev.domain.utils

object StringUtils {
    val String.Companion.EMPTY: String get() = ""
}

interface Foo {
    fun foo()
}

open class Bar: Foo {
    override fun foo() {
        TODO("Not yet implemented")
    }

}

class BarBar: Bar() {
    override fun foo() {
        super.foo()
    }
}