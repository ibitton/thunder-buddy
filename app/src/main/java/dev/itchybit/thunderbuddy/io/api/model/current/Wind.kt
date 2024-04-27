package dev.itchybit.thunderbuddy.io.api.model.current

import kotlinx.serialization.Serializable

@Serializable
data class Wind(
    val speed: Float?,
    val degree: Int?,
    val gust: Float?
)
