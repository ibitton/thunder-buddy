package dev.itchybit.thunderbuddy

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.itchybit.thunderbuddy.io.api.ApiResponse
import dev.itchybit.thunderbuddy.io.repo.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val json: Json, private val weatherRepository: WeatherRepository
) : ViewModel() {

    fun getCurrentWeather(latitude: String, longitude: String) =
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.getCurrentWeather(latitude, longitude).collect {
                when (it) {
                    is ApiResponse.Error -> {}
                    is ApiResponse.Success -> {
                        Log.d("VM", json.encodeToString(it.body))
                    }
                }
            }
        }

    fun get5DayForecast(latitude: String, longitude: String) =
        viewModelScope.launch(Dispatchers.IO) {
            weatherRepository.get5DayForecast(latitude, longitude).collect {
                when (it) {
                    is ApiResponse.Error -> {}
                    is ApiResponse.Success -> {
                        Log.d("VM", json.encodeToString(it.body))
                    }
                }
            }
        }
}