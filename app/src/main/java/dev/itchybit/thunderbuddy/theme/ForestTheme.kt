package dev.itchybit.thunderbuddy.theme

import dev.itchybit.thunderbuddy.R
import dev.itchybit.thunderbuddy.io.api.model.current.WeatherType

data object ForestTheme : WeatherTheme {

    override fun getBackgroundImage(weatherType: WeatherType): Int = when (weatherType) {
        WeatherType.SUNNY -> R.drawable.forest_sunny
        WeatherType.CLOUDY -> R.drawable.forest_cloudy
        WeatherType.RAINY -> R.drawable.forest_rainy
    }

    override fun getStatusBarColor(weatherType: WeatherType): Int = when (weatherType) {
        WeatherType.SUNNY -> R.color.forest_sunny_status
        WeatherType.CLOUDY -> R.color.forest_cloudy_status
        WeatherType.RAINY -> R.color.forest_rainy_status
    }

    override fun getBackgroundColor(weatherType: WeatherType): Int = when (weatherType) {
        WeatherType.SUNNY -> R.color.forest_sunny_background
        WeatherType.CLOUDY -> R.color.forest_cloudy_background
        WeatherType.RAINY -> R.color.forest_rainy_background
    }
}
