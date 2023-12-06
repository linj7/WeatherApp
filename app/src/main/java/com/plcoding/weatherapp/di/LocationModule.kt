package com.plcoding.weatherapp.di

import com.plcoding.weatherapp.data.location.DefaultLocationTracker
import com.plcoding.weatherapp.domain.location.LocationTracker
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Singleton

// 任何需要 LocationTracker 的地方都会自动获得 DefaultLocationTracker 的单例实例
@ExperimentalCoroutinesApi
@Module
@InstallIn(SingletonComponent::class)
abstract class LocationModule {

    /**
     * @Binds 是 Dagger 的一个注解，用于将一个接口的实现绑定到接口上。这样，当 Dagger 需要提供接口的实例时，它会自动提供这个注解所指定的实现。
     * 在这个例子中，@Binds 注解告诉 Dagger，当需要 LocationTracker 接口的实例时，应该提供 DefaultLocationTracker 类的实例。
     */
    @Binds
    @Singleton
    abstract fun bindLocationTracker(defaultLocationTracker: DefaultLocationTracker): LocationTracker
}