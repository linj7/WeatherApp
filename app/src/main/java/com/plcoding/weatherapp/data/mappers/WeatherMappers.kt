package com.plcoding.weatherapp.data.mappers

import com.plcoding.weatherapp.data.remote.WeatherDataDto
import com.plcoding.weatherapp.data.remote.WeatherDto
import com.plcoding.weatherapp.domain.weather.WeatherData
import com.plcoding.weatherapp.domain.weather.WeatherInfo
import com.plcoding.weatherapp.domain.weather.WeatherType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

private data class IndexedWeatherData(
    val index: Int,
    val data: WeatherData
)

/**
 * 该函数可以被WeatherDataDto类型的任何实例调用，返回一个Map类型的对象
 * 将WeatherDataDto对象中提取出数据，然后封装进WeatherData中
 *
 * 该函数是WeatherDataDto的一个扩展函数
 * 有别于常规函数，常规函数是直接定义在类内部的函数
 * 而扩展函数需要指定类名作为前缀，使用扩展函数是一种向类添加功能而不直接更改其源代码的方法，经常会看到扩展函数被统一组织在单独的文件中
 */
fun WeatherDataDto.toWeatherDataMap(): Map<Int, List<WeatherData>> {
    // 遍历WeatherDataDto中的time列表，对于每个时间元素，使用mapIndexed函数同时获取元素的索引和值
    // mapIndexed函数只会返回它的lambda表达式最后创建的IndexedWeatherData对象
    // 原本得到的JSON response（即对应WeatherDataDto）是time[...], temperature[...], pressure[...]各自一个数组
    // 经过mapIndexed函数后变成[{0, temperature[0], pressure[0]}, {1, temperature[1], pressure[1]}]
    return time.mapIndexed { index, time ->
        val temperature = temperatures[index]
        val weatherCode = weatherCodes[index]
        val windSpeed = windSpeeds[index]
        val pressure = pressures[index]
        val humidity = humidities[index]
        IndexedWeatherData(
            index = index,
            data = WeatherData(
                time = LocalDateTime.parse(time, DateTimeFormatter.ISO_DATE_TIME),
                temperatureCelsius = temperature,
                pressure = pressure,
                windSpeed = windSpeed,
                humidity = humidity,
                weatherType = WeatherType.fromWMO(weatherCode)
            )
        )
    }.groupBy {
        // it指代当前处理的元素，这里指代mapIndexed返回的IndexedWeatherData列表的每个元素
        // .groupBy会以it.index/24作为键，对应的值是由键相同的构成的列表
        it.index / 24
    }.mapValues {
        // 这里的it是指由groupBy生成的每个键值对
        // 将索引移除，只有WeatherData在最终的结果中
        it.value.map { it.data }
    }
}

fun WeatherDto.toWeatherInfo(): WeatherInfo {
    val weatherDataMap = weatherData.toWeatherDataMap()
    val now = LocalDateTime.now()

    // find函数是用来查找集合中第一个符合条件的元素
    val currentWeatherData = weatherDataMap[0]?.find {
        val hour = if(now.minute < 30) now.hour else now.hour + 1
        it.time.hour == hour
    }
    return WeatherInfo(
        weatherDataPerDay = weatherDataMap,
        currentWeatherData = currentWeatherData
    )
}