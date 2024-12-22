package com.example.twitter.data.cititesrepo

import com.banquemisr.challenge05.data.source.remote.TwitterEndPoint
import com.example.twitter.data.Constants.TEXT
import com.example.twitter.data.model.AuthResponseToken
import com.example.twitter.data.model.StateClass
import com.example.twitter.data.model.TweetResponse
import com.skydoves.sandwich.ApiResponse
import com.twitter.sdk.android.core.Callback
import com.twitter.sdk.android.core.TwitterCore
import com.twitter.sdk.android.core.TwitterException
import com.twitter.sdk.android.core.models.Tweet
import javax.inject.Inject
import kotlin.collections.HashMap

class TwitterRepoImplementer @Inject constructor(
     val webService: TwitterEndPoint
) : TwitterRepo {
   override suspend fun postTweet(tweetText : String
    ): ApiResponse<TweetResponse> {
       val res = webService.postTweet(HashMap<String, String>().also {
           it.put(TEXT,tweetText)
       })
        return res
    }

    override suspend fun postTweetUsingTwitterSdk(tweetText: String, function: (StateClass) -> Unit): StateClass {
        val client = TwitterCore.getInstance().apiClient
        val statusesService = client.statusesService
        statusesService.update(tweetText, null, false, null, null, null, false, false, null)
            .enqueue(object : Callback<Tweet>() {
                override fun success(result: com.twitter.sdk.android.core.Result<Tweet>?) {
                    function.invoke(StateClass.Success())
                }

                override fun failure(exception: TwitterException) {
                    function.invoke(StateClass.Error(exception.message.toString()))
                }
            })
       return StateClass.Loading()
    }

    override suspend fun getAuthToken(hashMap: HashMap<String, Any>): ApiResponse<AuthResponseToken> {
        val res = webService.postAuthTokenToGetAccessToken(hashMap)
        return res
    }

}

