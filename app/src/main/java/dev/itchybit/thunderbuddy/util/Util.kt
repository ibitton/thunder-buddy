package dev.itchybit.thunderbuddy.util

operator fun List<IntRange>.contains(element: Int?): Boolean = element != null && any {
    it.contains(element)
}