package dev.itchybit.thunderbuddy.io.repo

import dev.itchybit.thunderbuddy.io.api.ApiResponse
import dev.itchybit.thunderbuddy.io.api.CommonApiResponse
import dev.itchybit.thunderbuddy.io.api.service.WeatherService
import dev.itchybit.thunderbuddy.io.db.Database
import dev.itchybit.thunderbuddy.io.db.entity.CurrentWeather
import dev.itchybit.thunderbuddy.io.db.entity.Forecast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface WeatherRepository {
    suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        units: WeatherService.Units = WeatherService.Units.METRIC
    ): Flow<CommonApiResponse<CurrentWeather>>

    suspend fun get5DayForecast(
        latitude: Double,
        longitude: Double,
        units: WeatherService.Units = WeatherService.Units.METRIC
    ): Flow<CommonApiResponse<Forecast>>
}

class WeatherRepositoryImpl(
    private val database: Database, private val weatherService: WeatherService
) : WeatherRepository {

    override suspend fun getCurrentWeather(
        latitude: Double, longitude: Double, units: WeatherService.Units
    ) = flow {
        database.getCurrentWeatherDao().getWeather(latitude, longitude)?.let {
            emit(ApiResponse.Success(it))
        }
        when (val response = weatherService.getCurrentWeather(latitude, longitude, units)) {
            is ApiResponse.Success -> {
                CurrentWeather(
                    timestamp = System.currentTimeMillis(),
                    lat = latitude,
                    lon = longitude,
                    name = response.body.name ?: "",
                    weatherResponse = response.body
                ).let {
                    emit(ApiResponse.Success(it))
                    database.getCurrentWeatherDao().insertOrUpdate(it)
                }
            }

            is ApiResponse.Error -> emit(response)
        }
    }

    override suspend fun get5DayForecast(
        latitude: Double, longitude: Double, units: WeatherService.Units
    ) = flow {
        database.getForecastsDao().getWeather(latitude, longitude)?.let {
            emit(ApiResponse.Success(it))
        }
        when (val response = weatherService.get5DayForecast(latitude, longitude, units)) {
            is ApiResponse.Success -> {
                Forecast(
                    timestamp = System.currentTimeMillis(),
                    lat = latitude,
                    lon = longitude,
                    name = response.body.city.name ?: "",
                    forecastResponse = response.body
                ).let {
                    emit(ApiResponse.Success(it))
                    database.getForecastsDao().insertOrUpdate(it)
                }
            }

            is ApiResponse.Error -> emit(response)
        }
    }
}