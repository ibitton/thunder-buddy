package dev.itchybit.thunderbuddy.io.db.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.itchybit.thunderbuddy.io.api.model.current.WeatherResponse
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "current_weather")
data class CurrentWeather(
    val timestamp: Long,
    val lat: Double,
    val lon: Double,
    @PrimaryKey val name: String,
    @Embedded(prefix = "res_") val weatherResponse: WeatherResponse
)