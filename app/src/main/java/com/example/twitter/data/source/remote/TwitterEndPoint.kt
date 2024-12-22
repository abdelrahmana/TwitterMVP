package com.banquemisr.challenge05.data.source.remote

import com.example.twitter.data.model.AuthResponseToken
import com.example.twitter.data.model.TweetResponse
import com.skydoves.sandwich.ApiResponse
import retrofit2.http.Body
import retrofit2.http.POST

interface TwitterEndPoint {
    @POST(tweetURL) // movie list
    suspend fun postTweet(@Body hashMap: HashMap<String,String>
    ): ApiResponse<TweetResponse>

    @POST(tweetURLAUTHTOKEN) // movie list
    suspend fun postAuthTokenToGetAccessToken(@Body hashMap: HashMap<String,Any>
    ): ApiResponse<AuthResponseToken>

    companion object {
        const val tweetURL="2/tweets"
        const val tweetURLAUTHTOKEN="2/oauth2/token"

    }
}