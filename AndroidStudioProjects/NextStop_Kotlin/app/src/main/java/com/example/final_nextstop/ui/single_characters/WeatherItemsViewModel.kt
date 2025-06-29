//package com.example.final_nextstop.ui.single_characters
//
//
//import androidx.lifecycle.LiveData
//import androidx.lifecycle.MutableLiveData
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.viewModelScope
//import com.example.final_nextstop.data.model.WeatherItem
//import com.example.final_nextstop.data.repository.WeatherItemRepository
//import dagger.hilt.android.lifecycle.HiltViewModel
//import kotlinx.coroutines.launch
//import javax.inject.Inject
//
//@HiltViewModel
//class WeatherItemsViewModel @Inject constructor(
//    private val repository: WeatherItemRepository
//) : ViewModel() {
//
//    val weatherItems: LiveData<List<WeatherItem>> = repository.getAllWeatherItems()
//
//    private val _selectedItem = MutableLiveData<WeatherItem?>()
//    val selectedItem: LiveData<WeatherItem?> get() = _selectedItem
//
//    fun setSelectedItem(item: WeatherItem?) {
//        _selectedItem.value = item
//    }
//
//    fun addWeatherItem(item: WeatherItem) {
//        viewModelScope.launch {
//            repository.addWeatherItem(item)
//        }
//    }
//
//    fun deleteWeatherItem(item: WeatherItem) {
//        viewModelScope.launch {
//            repository.deleteWeatherItem(item)
//        }
//    }
//
//    fun deleteAllWeatherItems() {
//        viewModelScope.launch {
//            repository.deleteAllWeatherItems()
//        }
//    }
//
//    fun updateWeatherItem(item: WeatherItem) {
//        viewModelScope.launch {
//            repository.updateWeatherItem(item)
//        }
//    }
//
//    // אם תשתמשי ב־Retrofit לטעינת נתונים מהאינטרנט
//    fun fetchWeatherByCity(cityName: String) {
//        viewModelScope.launch {
//            repository.fetchWeatherFromApi(cityName)
//        }
//    }
//}
