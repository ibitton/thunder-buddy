package dev.itchybit.thunderbuddy.theme

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import dev.itchybit.thunderbuddy.io.api.model.current.WeatherType

sealed interface WeatherTheme {
    @DrawableRes
    fun getBackgroundImage(weatherType: WeatherType): Int

    @ColorRes
    fun getStatusBarColor(weatherType: WeatherType): Int

    @ColorRes
    fun getBackgroundColor(weatherType: WeatherType): Int
}
