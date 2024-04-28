package dev.itchybit.thunderbuddy.io.api.model.current

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Main(
    val temp: Float?,
    @SerialName("feels_like") val feelsLike: Float?,
    @SerialName("temp_min") val tempMin: Float?,
    @SerialName("temp_max") val tempMax: Float?,
    val pressure: Int?,
    val humidity: Int?,
    @SerialName("sea_level") val seaLevel: Int?,
    @SerialName("grnd_level") val groundLevel: Int?,
    @SerialName("temp_kf") val tempKf: Float?
)