package com.plcoding.weatherapp.di

import android.app.Application
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.plcoding.weatherapp.data.remote.WeatherApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

/**
 * 使用Dagger或Hilt这样的依赖注入框架时的Kotlin语法
 *
 * 假设类A需要一个B类的实例作为依赖项
 * 1. 定义依赖关系：在类A的构造函数中可以通过构造函数注入的方法表明这个依赖关系
 * 2. 定义提供方法：在Dagger模块中，需要定义一个方法来提供B类的实例。通过使用@Provides注解来告诉Dagger如何创建B的实例
 * 3. Dagger自动解析依赖：当定义了依赖关系和提供方法后，Dagger就会自动注入这些依赖项。例如当请求Dagger提供一个 A 的实例时，Dagger会查看 A 的构造函数，发现它需要一个 B 的实例。Dagger随后会查找模块中提供 B 实例的方法，如果找到了，它会先创建 B 的实例，然后使用这个实例来创建 A 的实例
 *
 * @Module 注解用于标记一个类，让Dagger直到在哪创建和提供依赖实例
 * @InstallIn 指定模块应该安装在哪个Hilt组件中
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides // 该注解用于标记一个方法，表明该方法会提供/创建某个依赖项的实例，这里会提供一个WeatherApi的实例
    @Singleton // 该注解表明提供的实例是单例的，即整个应用中只有一个WeatherApi的实例被创建和使用，避免资源浪费
    fun provideWeatherApi(): WeatherApi {
        return Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }

    // 通过在Dagger模块中提供这个方法，你可以在应用的其他部分轻松地通过依赖注入使用 FusedLocationProviderClient 实例，而无需在每个地方手动创建它
    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(app: Application): FusedLocationProviderClient {
        // FusedLocationProviderClient是Google Play服务提供的一个库，用于获取位置信息
        return LocationServices.getFusedLocationProviderClient(app)
    }
}