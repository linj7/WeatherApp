package com.plcoding.weatherapp.presentation.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

/**
 * 定义主题组件，如果想让某个组件的所有元素都运用这个主题，就把这个组件作为参数传给WeatherAppTheme
 */
@Composable
fun WeatherAppTheme(content: @Composable () -> Unit) {
    /**
     * MaterialTheme是Compose的内置函数
     */
    MaterialTheme(
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}