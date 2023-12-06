package com.plcoding.weatherapp.domain.weather

data class WeatherInfo(
    val weatherDataPerDay: Map<Int, List<WeatherData>>,

    // 使用问号结尾表明该类型是可空的，即null
    val currentWeatherData: WeatherData?
)
