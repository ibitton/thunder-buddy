package dev.itchybit.thunderbuddy.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.itchybit.thunderbuddy.R
import dev.itchybit.thunderbuddy.databinding.ItemForecastBinding
import dev.itchybit.thunderbuddy.io.api.model.current.WeatherResponse
import dev.itchybit.thunderbuddy.io.api.model.current.WeatherType
import java.time.format.TextStyle
import java.util.Locale

class ForecastAdapter :
    ListAdapter<WeatherResponse, ForecastAdapter.ViewHolder>(ForecastDiffCallback) {

    class ViewHolder(private val binding: ItemForecastBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(weatherResponse: WeatherResponse) = with(binding) {
            day.text =
                weatherResponse.getDayOfWeek()?.getDisplayName(TextStyle.FULL, Locale.getDefault())
            icon.setImageResource(
                when (weatherResponse.toWeatherType()) {
                    WeatherType.SUNNY -> R.drawable.clear
                    WeatherType.CLOUDY -> R.drawable.partlysunny
                    WeatherType.RAINY -> R.drawable.rain
                }
            )
            temp.text = binding.root.resources.getString(
                R.string.degree, weatherResponse.main?.temp?.toInt().toString()
            )
        }
    }

    object ForecastDiffCallback : DiffUtil.ItemCallback<WeatherResponse>() {
        override fun areItemsTheSame(oldItem: WeatherResponse, newItem: WeatherResponse): Boolean =
            oldItem == newItem

        override fun areContentsTheSame(
            oldItem: WeatherResponse, newItem: WeatherResponse
        ): Boolean = oldItem.dt == newItem.dt
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder(ItemForecastBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))
}