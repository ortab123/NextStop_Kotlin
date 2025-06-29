package com.example.final_nextstop.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.final_nextstop.data.model.WeatherItem
import com.example.final_nextstop.data.repository.WeatherItemRepository
import com.example.final_nextstop.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherItemRepository
) : ViewModel() {

    val weatherItems: LiveData<List<WeatherItem>> = repository.getAllWeatherItems()

    private val _selectedWeatherItem = MutableLiveData<WeatherItem?>()
    val selectedWeatherItem: LiveData<WeatherItem?> get() = _selectedWeatherItem

    private val _weatherFetchStatus = MutableLiveData<Resource<Unit>>()
    val weatherFetchStatus: LiveData<Resource<Unit>> get() = _weatherFetchStatus

//    fun setSelectedWeatherItem(item: WeatherItem) {
//        _selectedWeatherItem.value = item
//    }

//    fun addWeatherItem(item: WeatherItem) {
//        viewModelScope.launch {
//            repository.addWeatherItem(item)
//        }
//    }

//    fun updateWeatherItem(item: WeatherItem) {
//        viewModelScope.launch {
//            repository.updateWeatherItem(item)
//        }
//    }

    fun deleteWeatherItem(item: WeatherItem) {
        viewModelScope.launch {
            repository.deleteWeatherItem(item)
        }
    }

    fun deleteAllWeatherItems() {
        viewModelScope.launch {
            repository.deleteAllWeatherItems()
        }
    }

    fun fetchWeatherForCity(city: String) {
        viewModelScope.launch {
            _weatherFetchStatus.value = Resource.loading()
            val result = repository.fetchWeatherFromApi(city)
            _weatherFetchStatus.value = result
        }
    }
    fun fetchForecast(city: String) {
        viewModelScope.launch {
            _weatherFetchStatus.value = Resource.loading()
            val result = repository.fetchFiveDayForecast(city)
            _weatherFetchStatus.value = result
        }
    }

    fun fetchAirPollutionForCapital(city: String) {
        viewModelScope.launch {
            _weatherFetchStatus.value = Resource.loading()
            val result = repository.fetchAirPollutionByCountry(city)
            _weatherFetchStatus.value = result
        }
    }
}

