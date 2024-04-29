package dev.itchybit.thunderbuddy.util

import android.location.Address
import android.location.Geocoder
import android.os.Build
import androidx.annotation.IntRange

object LocationUtil {
    fun Geocoder.getFromLocationNameCompat(
        query: String, @IntRange maxResults: Int, listener: (MutableList<Address>?) -> Unit
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getFromLocationName(query, maxResults) {
                listener(it)
            }
        } else {
            listener.invoke(getFromLocationName(query, maxResults))
        }
    }
}