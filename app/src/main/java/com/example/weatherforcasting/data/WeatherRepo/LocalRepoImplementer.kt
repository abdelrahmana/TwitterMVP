package com.example.weatherforcasting.data.cititesrepo

import com.banquemisr.challenge05.data.source.remote.LocalEndPoint
import com.skydoves.sandwich.ApiResponse
import javax.inject.Inject

class LocalRepoImplementer @Inject constructor(
     val webService: LocalEndPoint
) : LocalNotification {
   override suspend fun getLocalNotification(
    ): ApiResponse<String> {
       val res = webService.getLocalNotification()
        return res
    }

}

