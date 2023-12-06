package com.plcoding.weatherapp.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * 调用了Retrofit库，该库常用于安卓开发中的HTTP客户端库，用于简化网络请求的过程
 */
interface WeatherApi {

    // Retrofit的GET注解，用来定义一个HTTP请求
    // suspend关键字标记这是一个挂起函数，用于在安卓应用中进行异步操作。即意味着这个函数会在不阻塞主线程的情况下执行
    // 函数格式
    // fun指定这是一个函数，之后跟函数名
    // 括号内是函数的参数，@Query注解用于指定该参数会附加到GET请求上作为URL查询参数。这里表明lat变量会将值传递给latitude查询参数
    // 最后冒号跟上的是返回类型，这里指定该函数会返回一个WeatherDto类型的对象
    @GET("v1/forecast?hourly=temperature_2m,weathercode,relativehumidity_2m,windspeed_10m,pressure_msl")
    suspend fun getWeatherData(
        @Query("latitude") lat: Double,
        @Query("longitude") long: Double
    ): WeatherDto
}