package com.example.twitter.data.model

data class TweetResponse(
    val data: TweetData?
)

data class TweetData(
    val id: String,
    val text: String
)