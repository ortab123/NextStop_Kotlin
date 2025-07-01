package com.example.final_nextstop.ui.all_characters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.final_nextstop.R
import com.example.final_nextstop.data.model.SearchType
import com.example.final_nextstop.data.model.WeatherItem
import com.example.final_nextstop.databinding.WeatherItemLayoutBinding
import com.example.final_nextstop.util.CapitalResolver
import java.util.Locale

class WeatherItemAdapter : RecyclerView.Adapter<WeatherItemAdapter.WeatherViewHolder>() {

    private val weatherItems = ArrayList<WeatherItem>()

    fun setWeatherItems(newItems: List<WeatherItem>) {
        weatherItems.clear()
        weatherItems.addAll(newItems)
        notifyDataSetChanged()
    }

    inner class WeatherViewHolder(private val binding: WeatherItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: WeatherItem) {

            binding.textViewCountryName.apply {
                if (Locale.getDefault().language == "iw") {
                    text = CapitalResolver.getCountryFromCapitalInHebrew(item.cityName) ?: item.cityName
                }
                else if (Locale.getDefault().language == "en"){
                    text = CapitalResolver.getCountryFromCapitalInEnglish(item.cityName) ?: item.cityName

                }
            }

            if (item.searchType == SearchType.TEMP_BY_COUNTRY) {
                binding.textViewAirPollution.visibility = View.GONE
                binding.textViewTemperature.visibility = View.VISIBLE
                binding.textViewDescription.visibility = View.GONE


                binding.textViewTemperature.apply {
                    text = context.getString(R.string.temperature_c, item.temperature)
                }
            }
            else if (item.searchType == SearchType.FORECAST_BY_COUNTRY) {
                binding.textViewAirPollution.visibility = View.GONE
                binding.textViewTemperature.visibility = View.GONE
                binding.textViewDescription.visibility = View.VISIBLE

                binding.textViewDescription.apply {
                    text = item.description
                }
            }
            else {
                binding.textViewAirPollution.visibility = View.VISIBLE
                binding.textViewTemperature.visibility = View.GONE
                binding.textViewDescription.visibility = View.GONE

                binding.textViewAirPollution.apply {
                    text = item.airPollution
                }
            }
        }
    }

    fun weatherItemAt(position: Int) = weatherItems[position]

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherViewHolder {
        val binding = WeatherItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: WeatherViewHolder, position: Int) {
        holder.bind(weatherItems[position])
    }

    override fun getItemCount(): Int = weatherItems.size
}
