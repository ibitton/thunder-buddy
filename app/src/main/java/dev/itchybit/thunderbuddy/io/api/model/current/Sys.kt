package dev.itchybit.thunderbuddy.io.api.model.current

import kotlinx.serialization.Serializable

@Serializable
data class Sys(
    val type: Int?,
    val id: Int?,
    val country: String?,
    val sunrise: Int?,
    val sunset: Int?,
    val pod: String?
)
