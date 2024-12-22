package com.example.twitter.data.cititesrepo

import com.example.twitter.data.model.AuthResponseToken
import com.example.twitter.data.model.StateClass
import com.example.twitter.data.model.TweetResponse
import com.skydoves.sandwich.ApiResponse

interface TwitterRepo {
    suspend fun postTweet(tweetText : String): ApiResponse<TweetResponse>
    suspend fun postTweetUsingTwitterSdk(tweetText: String, function: (StateClass) -> Unit): StateClass
    suspend fun getAuthToken(hashMap: HashMap<String,Any>): ApiResponse<AuthResponseToken>

}