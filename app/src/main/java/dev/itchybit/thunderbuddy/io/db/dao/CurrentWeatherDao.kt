package dev.itchybit.thunderbuddy.io.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.itchybit.thunderbuddy.io.db.entity.CurrentWeather

@Dao
interface CurrentWeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(currentWeather: CurrentWeather)

    @Query("SELECT * FROM current_weather WHERE lat = :lat AND lon = :lon LIMIT 1")
    fun getWeather(lat: Double, lon: Double): CurrentWeather?
}