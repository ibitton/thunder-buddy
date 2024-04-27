package dev.itchybit.thunderbuddy.io.api.model.current

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Volume(
    @SerialName("1h") val oneHour: Float?,
    @SerialName("3h") val threeHours: Float?
)
