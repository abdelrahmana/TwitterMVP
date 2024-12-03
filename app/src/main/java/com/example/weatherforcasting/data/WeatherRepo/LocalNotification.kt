package com.example.weatherforcasting.data.cititesrepo

import com.example.weatherforcasting.data.model.WeatherDataMigration
import com.skydoves.sandwich.ApiResponse

interface LocalNotification {
    suspend fun getLocalNotification(): ApiResponse<String>
}