package com.example.twitter.data.model
import com.google.gson.annotations.SerializedName


data class ErrorResponse(
    @SerializedName("detail")
    var detail: String?,
    @SerializedName("status")
    val status: Int?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("type")
    val type: String?
)