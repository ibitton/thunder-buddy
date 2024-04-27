package dev.itchybit.thunderbuddy.io.repo

import dev.itchybit.thunderbuddy.io.api.ApiResponse
import dev.itchybit.thunderbuddy.io.api.CommonApiResponse
import dev.itchybit.thunderbuddy.io.api.model.current.WeatherResponse
import dev.itchybit.thunderbuddy.io.api.model.forecast.ForecastResponse
import dev.itchybit.thunderbuddy.io.api.service.WeatherService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface WeatherRepository {
    suspend fun getCurrentWeather(
        latitude: String, longitude: String
    ): Flow<CommonApiResponse<WeatherResponse>>

    suspend fun get5DayForecast(
        latitude: String, longitude: String
    ): Flow<CommonApiResponse<ForecastResponse>>
}

class WeatherRepositoryImpl(private val weatherService: WeatherService) : WeatherRepository {

    override suspend fun getCurrentWeather(
        latitude: String, longitude: String
    ) = flow {
        when (val response = weatherService.getCurrentWeather(latitude, longitude)) {
            is ApiResponse.Success -> emit(response)
            is ApiResponse.Error -> emit(response)
        }
    }

    override suspend fun get5DayForecast(
        latitude: String, longitude: String
    ) = flow {
        when (val response = weatherService.get5DayForecast(latitude, longitude)) {
            is ApiResponse.Success -> emit(response)
            is ApiResponse.Error -> emit(response)
        }
    }

}