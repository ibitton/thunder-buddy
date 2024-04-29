package dev.itchybit.thunderbuddy.io.db.converter

import androidx.room.TypeConverter
import dev.itchybit.thunderbuddy.io.api.model.current.Weather
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object WeatherListConverter : Converter<List<Weather>>() {

    @TypeConverter
    override fun fromString(string: String): List<Weather> =
        Json.decodeFromString<List<Weather>>(string)

    @TypeConverter
    override fun toString(data: List<Weather>): String = Json.encodeToString(data)
}