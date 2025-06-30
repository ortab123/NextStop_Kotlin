package com.example.final_nextstop.ui.add_character

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.final_nextstop.R
import com.example.final_nextstop.databinding.AddWeatherItemLayoutBinding
import com.example.final_nextstop.ui.WeatherViewModel
import com.example.final_nextstop.util.CapitalResolver
import com.example.final_nextstop.util.autoCleared
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class AddWeatherItemFragment : Fragment() {

    private var _binding: AddWeatherItemLayoutBinding by autoCleared()
    private val binding get() = _binding
    private val viewModel: WeatherViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = AddWeatherItemLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val countries = mutableListOf(getString(R.string.select_location)) +
                Locale.getAvailableLocales().map { Locale("", it.country).displayCountry }
                    .filter { it.isNotBlank() }.distinct().sorted()

        val spinnerAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, countries)
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCountriesAddWeatherItem.adapter = spinnerAdapter

        binding.radioGroupOptions.setOnCheckedChangeListener { _, checkedId ->
            binding.spinnerCountriesAddWeatherItem.visibility = if (checkedId != -1) View.VISIBLE else View.GONE
            binding.labelSpinner.visibility = binding.spinnerCountriesAddWeatherItem.visibility
        }

        binding.btnBackFromAddWeather.setOnClickListener {
            findNavController().navigate(R.id.action_addWeatherItemFragment_to_weatherFragment)
        }

        binding.btnSubmit.setOnClickListener {
            val selectedOption = binding.radioGroupOptions.checkedRadioButtonId
            val country = binding.spinnerCountriesAddWeatherItem.selectedItem?.toString()
            var capital = CapitalResolver.getCapitalFromCountryInEnglish(country ?: "")

            if (Locale.getDefault().language == "iw") {
                capital = CapitalResolver.getCapitalFromCountryInHebrew(country ?: "")
            }

            if (selectedOption == -1) {
                toast("Please select an action")
                return@setOnClickListener
            }

            if (capital.isNullOrBlank()) {
                if(binding.spinnerCountriesAddWeatherItem.selectedItem=="Select location")
                {
                    toast("Please choose a country")
                }
                else{
                    toast("No valid capital city was found")
                }
                return@setOnClickListener
            }

            when (selectedOption) {
                binding.radioTempByCountry.id -> {
                    viewModel.fetchWeatherForCity(capital)
                    toast("Weather in $country")
                }
                binding.radioForecastByCountry.id -> {
                    viewModel.fetchForecast(capital)
                    toast("Forecast in $country")
                }
                binding.radioAirPollutionByCountry.id -> {


                    viewModel.fetchAirPollutionForCapital(capital)
                    toast("Air pollution at $country")
                }
                else -> toast("Please select an action")
            }

            findNavController().popBackStack()
        }
    }

    private fun toast(msg: String) {
        Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
    }
}
