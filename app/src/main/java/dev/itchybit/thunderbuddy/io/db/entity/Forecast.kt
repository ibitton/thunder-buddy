package dev.itchybit.thunderbuddy.io.db.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import dev.itchybit.thunderbuddy.io.api.model.forecast.ForecastResponse
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "forecasts")
data class Forecast(
    val timestamp: Long,
    val lat: Double,
    val lon: Double,
    @PrimaryKey val name: String,
    @Embedded val forecastResponse: ForecastResponse
)
