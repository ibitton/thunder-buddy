package dev.itchybit.thunderbuddy.io.db.converter

import androidx.room.TypeConverter
import dev.itchybit.thunderbuddy.io.api.model.current.WeatherResponse
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object WeatherResponseListConverter : Converter<List<WeatherResponse>>() {

    @TypeConverter
    override fun fromString(string: String): List<WeatherResponse> =
        Json.decodeFromString<List<WeatherResponse>>(string)

    @TypeConverter
    override fun toString(data: List<WeatherResponse>): String = Json.encodeToString(data)
}