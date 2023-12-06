package com.plcoding.weatherapp.presentation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.plcoding.weatherapp.domain.location.LocationTracker
import com.plcoding.weatherapp.domain.repository.WeatherRepository
import com.plcoding.weatherapp.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel // 这种ViewModel可以访问由Hilt管理的依赖项
class WeatherViewModel @Inject constructor( // 依赖注入，表示该构造函数依赖的参数会由Hilt自动注入
    private val repository: WeatherRepository,
    private val locationTracker: LocationTracker
): ViewModel() { // 继承自ViewModel类

    // 状态是指那些可以改变，并且会导致UI重新渲染的数据
    // mutableStateOf用于创建一个可观察的状态对象
    // 当这个状态对象的值改变时，任何使用这个状态的都会被重新调用，从而更新UI
    var state by mutableStateOf(WeatherState())
        private set // 意味着不能从类的外部更改这个状态

    fun loadWeatherInfo() {
        viewModelScope.launch {
            state = state.copy(
                isLoading = true,
                error = null
            )
            locationTracker.getCurrentLocation()?.let { location ->
                when(val result = repository.getWeatherData(location.latitude, location.longitude)) {
                    is Resource.Success -> {
                        state = state.copy(
                            weatherInfo = result.data,
                            isLoading = false,
                            error = null
                        )
                    }
                    is Resource.Error -> {
                        state = state.copy(
                            weatherInfo = null,
                            isLoading = false,
                            error = result.message
                        )
                    }
                }
            } ?: kotlin.run {
                state = state.copy(
                    isLoading = false,
                    error = "Couldn't retrieve location. Make sure to grant permission and enable GPS."
                )
            }
        }
    }
}