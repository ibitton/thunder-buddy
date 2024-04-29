package dev.itchybit.thunderbuddy.io.api.model.current

import androidx.room.ColumnInfo
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Volume(
    @ColumnInfo(name = "1h") @SerialName("1h") val oneHour: Float?,
    @ColumnInfo(name = "3h") @SerialName("3h") val threeHours: Float?
)
