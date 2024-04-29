package dev.itchybit.thunderbuddy.io.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.itchybit.thunderbuddy.io.db.converter.WeatherListConverter
import dev.itchybit.thunderbuddy.io.db.converter.WeatherResponseListConverter
import dev.itchybit.thunderbuddy.io.db.dao.CurrentWeatherDao
import dev.itchybit.thunderbuddy.io.db.dao.FavouritesDao
import dev.itchybit.thunderbuddy.io.db.dao.ForecastsDao
import dev.itchybit.thunderbuddy.io.db.entity.CurrentWeather
import dev.itchybit.thunderbuddy.io.db.entity.Favourite
import dev.itchybit.thunderbuddy.io.db.entity.Forecast

@Database(entities = [Favourite::class, CurrentWeather::class, Forecast::class], version = 4)
@TypeConverters(WeatherListConverter::class, WeatherResponseListConverter::class)
abstract class Database : RoomDatabase() {

    companion object {
        const val DB_NAME = "weather_db"
    }

    abstract fun getFavouritesDao(): FavouritesDao
    abstract fun getCurrentWeatherDao(): CurrentWeatherDao
    abstract fun getForecastsDao(): ForecastsDao
}