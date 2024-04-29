package dev.itchybit.thunderbuddy.io.api.service

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.itchybit.thunderbuddy.io.api.model.current.WeatherResponse
import dev.itchybit.thunderbuddy.io.api.model.forecast.ForecastResponse
import dev.itchybit.thunderbuddy.util.NetworkUtil.safeCommonRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.url
import io.ktor.http.HttpMethod
import javax.inject.Inject

class WeatherService @Inject constructor(
    @ApplicationContext private val context: Context,
    private val httpClient: HttpClient
) {

    companion object {
        private const val API_KEY = "7d0ebd8402bebeeaf87eea959f7eeb5d"
    }

    enum class Units { STANDARD, METRIC, IMPERIAL }

    suspend fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        units: Units = Units.METRIC
    ) =
        httpClient.safeCommonRequest<WeatherResponse> {
            method = HttpMethod.Get
            url("weather?lat=$latitude&lon=$longitude&units=$units&appid=$API_KEY")
        }

    suspend fun get5DayForecast(latitude: Double, longitude: Double, units: Units = Units.METRIC) =
        httpClient.safeCommonRequest<ForecastResponse> {
            method = HttpMethod.Get
            url("forecast?lat=$latitude&lon=$longitude&units=$units&appid=$API_KEY")
        }
}
