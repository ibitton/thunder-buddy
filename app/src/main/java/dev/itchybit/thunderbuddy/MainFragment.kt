package dev.itchybit.thunderbuddy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dev.itchybit.thunderbuddy.databinding.FragmentMainBinding
import dev.itchybit.thunderbuddy.io.api.model.current.WeatherResponse
import dev.itchybit.thunderbuddy.io.api.model.current.WeatherType
import dev.itchybit.thunderbuddy.theme.ForestTheme
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupUi() = with(binding) {
        forecastAdapter = ForecastAdapter()
        forecast.adapter = forecastAdapter
    }

    private fun observeViewModel() = with(viewModel) {
        currentWeather.observe(viewLifecycleOwner) { weatherResponse ->
            binding.root.visibility = View.VISIBLE
            setupUiText(weatherResponse)
            weatherResponse.toWeatherType().let {
                setupBackgroundImage(it)
                setupStatusBarColor(it)
                setupBackgroundColor(it)
            }
        }

        forecast.observe(viewLifecycleOwner) { forecast ->
            val hashMap = LinkedHashMap<DayOfWeek, WeatherResponse>()
            forecast.list?.forEach { weatherResponse ->
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

    private fun setupUiText(weatherResponse: WeatherResponse) = with(binding) {
        currentWeather.text =
            getString(R.string.degree, weatherResponse.main?.temp?.toInt().toString())
        minTemp.text = getString(R.string.degree, weatherResponse.main?.tempMin?.toInt().toString())
        maxTemp.text = getString(R.string.degree, weatherResponse.main?.tempMax?.toInt().toString())
        currentTemp.text =
            getString(R.string.degree, weatherResponse.main?.temp?.toInt().toString())
        currentWeatherType.text = weatherResponse.weather?.get(0)?.description
        name.text = weatherResponse.name
    }

    private fun setupBackgroundImage(weatherType: WeatherType) = with(binding.weatherBackground) {
        setImageResource(ForestTheme.getBackgroundImage(weatherType))
    }

    private fun setupStatusBarColor(weatherType: WeatherType) = with(requireActivity().window) {
        addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        statusBarColor =
            ContextCompat.getColor(requireContext(), ForestTheme.getStatusBarColor(weatherType))
    }

    private fun setupBackgroundColor(weatherType: WeatherType) = with(binding.container) {
        setBackgroundColor(
            ContextCompat.getColor(
                requireContext(), ForestTheme.getBackgroundColor(weatherType)
            )
        )
    }
}