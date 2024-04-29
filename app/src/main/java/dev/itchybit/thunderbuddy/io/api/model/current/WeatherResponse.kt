package dev.itchybit.thunderbuddy.io.api.model.current

import androidx.room.Embedded
import dev.itchybit.thunderbuddy.util.contains
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.time.DayOfWeek
import java.time.Instant
import java.time.ZoneId

enum class WeatherType { SUNNY, CLOUDY, RAINY }
sealed class WeatherCode(val ranges: List<IntRange>) {
    data object Thunderstorm : WeatherCode(listOf(200..202, 210..212, 221..221, 230..232))
    data object Drizzle : WeatherCode(listOf(300..302, 310..314, 321..321))
    data object Rain : WeatherCode(listOf(500..504, 511..511, 520..522, 531..531))
    data object Snow : WeatherCode(listOf(600..602, 611..613, 615..616, 620..622))
    data object Atmosphere : WeatherCode(
        listOf(
            701..701, 711..711, 721..721, 731..731, 741..741, 751..751, 761..762, 771..771, 781..781
        )
    )

    data object Clear : WeatherCode(listOf(800..800))
    data object Clouds : WeatherCode(listOf(801..804))
}

@Serializable
data class WeatherResponse(
    @Embedded val coord: Coord?,
    val weather: List<Weather>?,
    val base: String?,
    @Embedded val main: Main?,
    val visibility: Int?,
    @Embedded val wind: Wind?,
    @Embedded(prefix = "rain_") val rain: Volume?,
    @Embedded(prefix = "snow_") val snow: Volume?,
    @Embedded val clouds: Clouds?,
    val pop: Float?,
    val dt: Long?,
    @SerialName("dt_text") val dtText: String?,
    @Embedded(prefix = "sys") val sys: Sys?,
    val timezone: Int?,
    val id: Int?,
    val name: String?,
    val cod: Int?
) {
    fun toWeatherType(): WeatherType = when (weather?.get(0)?.id) {
        in (WeatherCode.Thunderstorm.ranges), in (WeatherCode.Drizzle.ranges), in (WeatherCode.Rain.ranges), in (WeatherCode.Snow.ranges) -> WeatherType.RAINY
        in (WeatherCode.Atmosphere.ranges), in (WeatherCode.Clouds.ranges) -> WeatherType.CLOUDY
        in (WeatherCode.Clear.ranges) -> WeatherType.SUNNY
        else -> WeatherType.SUNNY
    }

    fun getDayOfWeek(): DayOfWeek? =
        dt?.let { Instant.ofEpochSecond(it).atZone(ZoneId.systemDefault()).dayOfWeek }
}
