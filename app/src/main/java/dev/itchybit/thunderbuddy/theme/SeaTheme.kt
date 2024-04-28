package dev.itchybit.thunderbuddy.theme

import dev.itchybit.thunderbuddy.R
import dev.itchybit.thunderbuddy.io.api.model.current.WeatherType

data object SeaTheme : WeatherTheme {

    override fun getBackgroundImage(weatherType: WeatherType): Int = when (weatherType) {
        WeatherType.SUNNY -> R.drawable.sea_sunny
        WeatherType.CLOUDY -> R.drawable.sea_cloudy
        WeatherType.RAINY -> R.drawable.sea_rainy
    }

    override fun getStatusBarColor(weatherType: WeatherType): Int = when (weatherType) {
        WeatherType.SUNNY -> R.color.sea_sunny_status
        WeatherType.CLOUDY -> R.color.sea_cloudy_status
        WeatherType.RAINY -> R.color.sea_rainy_status
    }

    override fun getBackgroundColor(weatherType: WeatherType): Int = when (weatherType) {
        WeatherType.SUNNY -> R.color.sea_sunny_background
        WeatherType.CLOUDY -> R.color.sea_cloudy_background
        WeatherType.RAINY -> R.color.sea_rainy_background
    }
}
