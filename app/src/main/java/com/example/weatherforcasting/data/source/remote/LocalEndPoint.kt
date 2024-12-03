package com.banquemisr.challenge05.data.source.remote

import com.skydoves.sandwich.ApiResponse
import retrofit2.http.GET

interface LocalEndPoint {
    @GET(GET_Notification) // movie list
    suspend fun getLocalNotification(
    ): ApiResponse<String>

    companion object {
        const val GET_Notification="localalerts.php"
    }
}