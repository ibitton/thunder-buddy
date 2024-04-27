package dev.itchybit.thunderbuddy.io.api.model.forecast

import dev.itchybit.thunderbuddy.io.api.model.City
import dev.itchybit.thunderbuddy.io.api.model.current.WeatherResponse
import kotlinx.serialization.Serializable

@Serializable
data class ForecastResponse(
    val cod: String?,
    val message: Int?,
    val cnt: Int?,
    val list: List<WeatherResponse>?,
    val city: City?
)
