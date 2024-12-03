package com.example.weatherforcasting.data.source

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.weatherforcasting.data.model.City
import com.example.weatherforcasting.data.RoomConverter
import com.example.weatherforcasting.data.model.WeatherItem

@Database(entities = [City::class, WeatherItem::class], version = 1)
@TypeConverters(RoomConverter::class)
abstract class AppDataBase : RoomDatabase() {



}