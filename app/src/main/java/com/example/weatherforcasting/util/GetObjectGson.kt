package com.linkme.cartiapp.util

import com.example.weatherforcasting.data.model.NotificationData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object GetObjectGson {
    fun getNotificationData(objectData : String): NotificationData? { // this should return the object
        val jso = objectData
        val gson = Gson()
        val typeToken = object : TypeToken<NotificationData?>() {}.type
        val obj = gson.fromJson<NotificationData>(jso, typeToken) ?: null
        return obj

    }

}