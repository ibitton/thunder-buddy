package dev.itchybit.thunderbuddy.io.api.model

import androidx.room.Embedded
import dev.itchybit.thunderbuddy.io.api.model.current.Coord
import kotlinx.serialization.Serializable

@Serializable
data class City(
    val id: Int?,
    val name: String?,
    @Embedded val coord: Coord?,
    val country: String?,
    val population: Int?,
    val timezone: Int?,
    val sunrise: Int?,
    val sunset: Int?
)
