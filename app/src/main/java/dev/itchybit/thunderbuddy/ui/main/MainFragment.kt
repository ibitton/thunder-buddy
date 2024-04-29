package dev.itchybit.thunderbuddy.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.preference.PreferenceManager
import dev.itchybit.thunderbuddy.R
import dev.itchybit.thunderbuddy.databinding.FragmentMainBinding
import dev.itchybit.thunderbuddy.io.api.model.current.WeatherResponse
import dev.itchybit.thunderbuddy.io.api.model.current.WeatherType
import dev.itchybit.thunderbuddy.io.api.service.WeatherService
import dev.itchybit.thunderbuddy.io.db.entity.CurrentWeather
import dev.itchybit.thunderbuddy.theme.ForestTheme
import dev.itchybit.thunderbuddy.theme.SeaTheme
import dev.itchybit.thunderbuddy.theme.WeatherTheme
import dev.itchybit.thunderbuddy.util.StringUtil
import java.time.DayOfWeek

class MainFragment : Fragment(R.layout.fragment_main) {

    private val viewModel: MainViewModel by activityViewModels()

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var forecastAdapter: ForecastAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupUi()
        observeViewModel()
    }

    override fun onResume() {
        super.onResume()

        viewModel.onMainFragmentResume()
    }

    override fun onPause() {
        super.onPause()

        viewModel.onMainFragmentPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupUi() = with(binding) {
        forecastAdapter = ForecastAdapter()
        forecast.adapter = forecastAdapter

        previousFavourite.setOnClickListener {
            viewModel.selectPreviousFavourite(getWeatherUnitsFromPreferences())
        }

        nextFavourite.setOnClickListener {
            viewModel.selectNextFavourite(getWeatherUnitsFromPreferences())
        }
    }

    private fun observeViewModel() = with(viewModel) {
        currentWeather.observe(viewLifecycleOwner) { currentWeather ->
            binding.root.visibility = View.VISIBLE
            setupUiText(currentWeather)
            currentWeather.weatherResponse.toWeatherType().let {
                setupBackgroundImage(it)
                setupStatusBarColor(it)
                setupBackgroundColor(it)
            }
        }

        forecast.observe(viewLifecycleOwner) { forecast ->
            val hashMap = LinkedHashMap<DayOfWeek, WeatherResponse>()
            forecast.forecastResponse.list?.forEach { weatherResponse ->
                weatherResponse.getDayOfWeek()?.let {
                    //update weather value for given day of week
                    if (hashMap[it] == null || hashMap[it]!!.main?.tempMax!! < weatherResponse.main?.tempMax!!) {
                        hashMap[it] = weatherResponse
                    }
                }
            }
            forecastAdapter.submitList(hashMap.values.toList())
        }
    }

    private fun setupUiText(weather: CurrentWeather) = with(binding) {
        currentWeather.text =
            getString(R.string.degree, weather.weatherResponse.main?.temp?.toInt().toString())
        minTemp.text =
            getString(R.string.degree, weather.weatherResponse.main?.tempMin?.toInt().toString())
        maxTemp.text =
            getString(R.string.degree, weather.weatherResponse.main?.tempMax?.toInt().toString())
        currentTemp.text =
            getString(R.string.degree, weather.weatherResponse.main?.temp?.toInt().toString())
        currentWeatherType.text = weather.weatherResponse.weather?.get(0)?.description
        name.text = weather.weatherResponse.name
        lastUpdated.text = getString(
            R.string.last_updated,
            StringUtil.convertMillisecondsToDisplayTime(System.currentTimeMillis() - weather.timestamp)
        )
    }

    private fun setupBackgroundImage(weatherType: WeatherType) = with(binding.weatherBackground) {
        setImageResource(getTheme().getBackgroundImage(weatherType))
    }

    private fun setupStatusBarColor(weatherType: WeatherType) = with(requireActivity().window) {
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        statusBarColor =
            ContextCompat.getColor(requireContext(), getTheme().getStatusBarColor(weatherType))
    }

    private fun setupBackgroundColor(weatherType: WeatherType) = with(binding.container) {
        setBackgroundColor(
            ContextCompat.getColor(
                requireContext(), getTheme().getBackgroundColor(weatherType)
            )
        )
    }

    private fun getTheme(): WeatherTheme =
        when (PreferenceManager.getDefaultSharedPreferences(requireContext())
            .getString("theme", "forest")) {
            "forest" -> ForestTheme
            "sea" -> SeaTheme
            else -> ForestTheme
        }

    private fun getWeatherUnitsFromPreferences(): WeatherService.Units =
        when (PreferenceManager.getDefaultSharedPreferences(requireContext())
            .getString("units", "metric")) {
            "standard" -> WeatherService.Units.STANDARD
            "metric" -> WeatherService.Units.METRIC
            "imperial" -> WeatherService.Units.IMPERIAL
            else -> WeatherService.Units.METRIC
        }
}