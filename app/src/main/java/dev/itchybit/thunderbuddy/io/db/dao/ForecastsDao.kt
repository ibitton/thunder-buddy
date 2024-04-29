package dev.itchybit.thunderbuddy.io.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import dev.itchybit.thunderbuddy.io.db.entity.Forecast

@Dao
interface ForecastsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertOrUpdate(forecast: Forecast)

    @Query("SELECT * FROM forecasts WHERE lat = :lat AND lon = :lon LIMIT 1")
    fun getWeather(lat: Double, lon: Double): Forecast?
}