package dev.itchybit.thunderbuddy.io.api.model.current

import dev.itchybit.thunderbuddy.io.api.model.City
import kotlinx.serialization.Serializable

@Serializable
data class WeatherResponse(
    val coord: Coord?,
    val weather: List<Weather>?,
    val base: String?,
    val main: Main?,
    val visibility: Int?,
    val wind: Wind?,
    val rain: Volume?,
    val snow: Volume?,
    val clouds: Clouds?,
    val dt: Int?,
    val sys: City?,
    val timezone: Int?,
    val id: Int?,
    val name: String?,
    val cod: Int?
)