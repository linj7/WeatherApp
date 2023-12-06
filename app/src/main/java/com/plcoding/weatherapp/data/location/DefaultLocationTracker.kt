package com.plcoding.weatherapp.data.location

import android.Manifest
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.plcoding.weatherapp.domain.location.LocationTracker
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume

@ExperimentalCoroutinesApi
class DefaultLocationTracker @Inject constructor(
    // @Inject注解会告诉依赖注入框架，当构造出一个DefaultLocaltionTracker实例时，应该为它自动提供依赖的FusedLocationProviderClient和Application的实例
    private val locationClient: FusedLocationProviderClient,
    private val application: Application
): LocationTracker {

    // 返回Location类型的对象，？表示也可以接受返回null
    override suspend fun getCurrentLocation(): Location? {
        // 检查是否有精确位置的权限
        // checkSelfPermission会返回权限的状态（被授权或未被授权）
        val hasAccessFineLocationPermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        // 检查是否有大致位置的权限
        val hasAccessCoarseLocationPermission = ContextCompat.checkSelfPermission(
            application,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        // 获取位置管理器服务
        val locationManager = application.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // 通过位置管理器服务检查网络定位服务（使用蜂窝数据或WIFI定位）或GPS定位服务是否启用
        val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        if(!hasAccessCoarseLocationPermission || !hasAccessFineLocationPermission || !isGpsEnabled) {
            return null
        }

        // suspendCancellableCoroutine是Kotlin协程库提供的一个api，专门用于在协程中处理挂起操作
        // 它能暂停/挂起当前协程的执行，直到异步操作完成（这里即直到位置请求完成）
        return suspendCancellableCoroutine { cont ->
            locationClient.lastLocation.apply {// 获取设备最后的已知位置
                if(isComplete) {
                    if(isSuccessful) {
                        cont.resume(result) // 若请求成功，恢复协程，传递结果
                    } else {
                        cont.resume(null) // 若请求成功，恢复协程，传递null值
                    }
                    return@suspendCancellableCoroutine // 用于从一个标签指定的函数中返回
                }

                // 若lastLocation请求还没完成，则添加成功、失败和取消的监听器
                addOnSuccessListener {
                    cont.resume(it)
                }
                addOnFailureListener {
                    cont.resume(null)
                }
                addOnCanceledListener {
                    cont.cancel() // 如果请求被取消，取消协程
                }
            }
        }
    }
}