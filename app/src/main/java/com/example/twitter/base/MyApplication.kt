package com.example.twitter

import android.app.Application
import android.util.Log
import com.example.twitter.data.Constants
import com.twitter.sdk.android.core.DefaultLogger
import com.twitter.sdk.android.core.Twitter
import com.twitter.sdk.android.core.TwitterAuthConfig
import com.twitter.sdk.android.core.TwitterConfig
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        // tried the sdk of twitter but it gives error in certificate pinning
        val config = TwitterConfig.Builder(this)
            .logger(DefaultLogger(Log.DEBUG)) // Enable logging
            .twitterAuthConfig(
                TwitterAuthConfig(
                    Constants.consumerKey,
                    Constants.consumerSecret
                )
            )
            .debug(true) // Enable debug mode
            .build()
        Twitter.initialize(config)
    }
}