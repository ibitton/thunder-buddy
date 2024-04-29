package dev.itchybit.thunderbuddy.util

object StringUtil {
    fun convertMillisecondsToDisplayTime(milliseconds: Long): String {
        if (milliseconds < 1000) {
            return "$milliseconds ms"
        } else if (milliseconds < 60_000) {
            return "${milliseconds / 1000} sec"
        } else if (milliseconds < 3_600_000) {
            return "${milliseconds / 60_000} min"
        } else {
            return "${milliseconds / 3_600_000} hours"
        }
    }
}