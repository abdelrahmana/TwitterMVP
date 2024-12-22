package com.example.twitter.util

import com.example.twitter.data.model.ErrorResponse
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.ResponseBody

object Util {
    fun getErrorBodyResponse(errorBody: ResponseBody?): String {
        val gson = Gson()
        var errorResponse: ErrorResponse? = null
        try {
            val type = object : TypeToken<ErrorResponse>() {}.type

            errorResponse = gson.fromJson(errorBody?.charStream(), type) ?: (ErrorResponse("",null,"",null)
                .also {
                    it.detail = "Error Happened"


                })
        } catch (e: Exception) {
            errorResponse = (ErrorResponse("",null,e.message.toString(),null)
                .also {
                    it.detail = e.message.toString()

                })
        }

        val error = errorResponse?.detail
        return error ?: ""

    }
}