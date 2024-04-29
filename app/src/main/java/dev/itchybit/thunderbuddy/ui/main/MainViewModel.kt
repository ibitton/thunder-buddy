package dev.itchybit.thunderbuddy.ui.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.itchybit.thunderbuddy.io.api.ApiResponse
import dev.itchybit.thunderbuddy.io.api.service.WeatherService
import dev.itchybit.thunderbuddy.io.db.entity.CurrentWeather
import dev.itchybit.thunderbuddy.io.db.entity.Favourite
import dev.itchybit.thunderbuddy.io.db.entity.Forecast
import dev.itchybit.thunderbuddy.io.repo.FavouritesRepository
import dev.itchybit.thunderbuddy.io.repo.WeatherRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val json: Json,
    private val weatherRepository: WeatherRepository,
    private val favouritesRepository: FavouritesRepository
) : ViewModel() {

    val currentWeather: MutableLiveData<CurrentWeather> by lazy { MutableLiveData<CurrentWeather>() }
    val forecast: MutableLiveData<Forecast> by lazy { MutableLiveData<Forecast>() }
    val favourites: MutableLiveData<List<Favourite>> by lazy { MutableLiveData<List<Favourite>>() }

    //cheap event imitation
    val onMainFragmentResumeEvent: MutableLiveData<Boolean> by lazy { MutableLiveData<Boolean>(false) }

    private var currentFavouriteIndex: Int = 0

    init {
        refreshFavourites()
    }

    fun getCurrentWeather(
        latitude: Double,
        longitude: Double,
        units: WeatherService.Units = WeatherService.Units.METRIC
    ) = viewModelScope.launch(Dispatchers.IO) {
        weatherRepository.getCurrentWeather(latitude, longitude, units).collect {
            when (it) {
                is ApiResponse.Success -> {
                    Log.d("VM", json.encodeToString(it.body))
                    currentWeather.postValue(it.body)
                }

                is ApiResponse.Error -> {}
            }
        }
    }

    fun get5DayForecast(
        latitude: Double,
        longitude: Double,
        units: WeatherService.Units = WeatherService.Units.METRIC
    ) = viewModelScope.launch(Dispatchers.IO) {
        weatherRepository.get5DayForecast(latitude, longitude, units).collect {
            when (it) {
                is ApiResponse.Success -> {
                    Log.d("VM", json.encodeToString(it.body))
                    forecast.postValue(it.body)
                }

                is ApiResponse.Error -> {}
            }
        }
    }

    fun addToFavourites(name: String, latitude: Double, longitude: Double) =
        viewModelScope.launch(Dispatchers.IO) {
            favouritesRepository.addToFavourites(
                Favourite(
                    name = name, latitude = latitude, longitude = longitude
                )
            ).collect {
                refreshFavourites()
            }
        }

    fun removeFavourite(favourite: Favourite) = viewModelScope.launch(Dispatchers.IO) {
        favouritesRepository.removeFavourite(favourite).collect {
            refreshFavourites()
        }
    }

    fun selectPreviousFavourite(units: WeatherService.Units = WeatherService.Units.METRIC) {
        if (favourites.value?.isNotEmpty() == true) {
            currentFavouriteIndex = (currentFavouriteIndex - 1).mod(favourites.value?.size!!)
            getCurrentFavouriteData(favourites.value?.get(currentFavouriteIndex)!!, units)
        }
    }

    fun selectNextFavourite(units: WeatherService.Units = WeatherService.Units.METRIC) {
        if (favourites.value?.isNotEmpty() == true) {
            currentFavouriteIndex = (currentFavouriteIndex + 1).mod(favourites.value?.size!!)
            getCurrentFavouriteData(favourites.value?.get(currentFavouriteIndex)!!, units)
        }
    }

    fun onMainFragmentPause() = onMainFragmentResumeEvent.postValue(false)
    fun onMainFragmentResume() = onMainFragmentResumeEvent.postValue(true)

    private fun getCurrentFavouriteData(
        favourite: Favourite, units: WeatherService.Units = WeatherService.Units.METRIC
    ) {
        getCurrentWeather(favourite.latitude, favourite.longitude, units)
        get5DayForecast(favourite.latitude, favourite.longitude, units)
    }

    private fun refreshFavourites() = viewModelScope.launch(Dispatchers.IO) {
        favouritesRepository.getAll().collect { favourites.postValue(it) }
    }
}