package com.plcoding.weatherapp.data.remote

import com.squareup.moshi.Json

/**
 * This class represents the JSON response we get from the api.
 * We only put the information we need in the app
 * Here we only need the 'hourly' field
 *
 * 表明这是Kotlin中的一个数据类，专门用来存储数据，它们会自动为你生成一些有用的方法，例如：equals(), hashCode(), toString(), 数据类通常用作网络响应的模型
 */
data class WeatherDto(
    // 定义了该类的一个属性，叫做weatherData，类型为WeatherDataDto
    // 该注解指定JSON中的'hourly'字段应该映射到weatherData属性上
    @field:Json(name = "hourly")
    val weatherData: WeatherDataDto
)
