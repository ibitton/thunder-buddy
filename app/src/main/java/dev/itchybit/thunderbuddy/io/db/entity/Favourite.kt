package dev.itchybit.thunderbuddy.io.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourites")
data class Favourite(@PrimaryKey val name: String, val latitude: Double, val longitude: Double)