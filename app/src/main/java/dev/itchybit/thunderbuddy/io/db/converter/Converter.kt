package dev.itchybit.thunderbuddy.io.db.converter

abstract class Converter<T> {
    abstract fun toString(data: T): String
    abstract fun fromString(string: String): T
}