package com.example.twitter.ui.twitter

import android.util.Log
import android.view.View
import androidx.lifecycle.viewModelScope
import com.example.twitter.base.BaseViewModel
import com.example.twitter.data.cititesrepo.TwitterRepo
import com.example.twitter.data.model.AuthResponseToken
import com.example.twitter.data.model.StateClass
import com.example.twitter.data.model.TweetResponse
import com.example.twitter.util.Util.getErrorBodyResponse
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onFailure
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class TwitterViewModel @Inject constructor(
    private val weatherRepo: TwitterRepo
) :
    BaseViewModel() {
    private val _errorMutable = MutableStateFlow<String?>(null)
    val errorStateFlow: StateFlow<String?> = _errorMutable
    private val _tweetPostedDataMutable = MutableStateFlow<TweetResponse?>(null)
    val tweetDataStateFlow: StateFlow<TweetResponse?> = _tweetPostedDataMutable
    private val _authTokenMutableState = MutableStateFlow<AuthResponseToken?>(null)
    val authTokenState: StateFlow<AuthResponseToken?> = _authTokenMutableState
    fun postTweetApi(
        tweets: String
    ) {
        setNetworkLoader(View.VISIBLE)
        viewModelScope.launch {
            val result = weatherRepo.postTweet(tweets)
            result.onSuccess {
                viewModelScope.launch(Dispatchers.IO) {
                    _tweetPostedDataMutable.emit(data)
                }
            }
            result.onFailure {
                viewModelScope.launch(Dispatchers.IO) {
                    _errorMutable.emit("error happened failed")
                }
            }
            result.onError {
                viewModelScope.launch(Dispatchers.IO) {
                    _errorMutable.emit(getErrorBodyResponse(errorBody))
                }
            }
            setNetworkLoader(View.GONE)

        }
    }
    fun postGetAccessTokenResponse(
        hashMap: HashMap<String,Any>
    ) {
        setNetworkLoader(View.VISIBLE)
        viewModelScope.launch {
            val result = weatherRepo.getAuthToken(hashMap)
            result.onSuccess {
                viewModelScope.launch(Dispatchers.IO) {
                    _authTokenMutableState.emit(data)
                }
            }
            result.onFailure {
                viewModelScope.launch(Dispatchers.IO) {
                    _errorMutable.emit("error happened failed")
                }
            }
            result.onError {
                Log.v("error", getErrorBodyResponse(errorBody))
            }
            setNetworkLoader(View.GONE)

        }
    }

    fun postTweetSdk(
        tweets: String
    ) {
        setNetworkLoader(View.VISIBLE)
        viewModelScope.launch {
            weatherRepo.postTweetUsingTwitterSdk(tweets) { result ->
                viewModelScope.launch(Dispatchers.IO)
                {
                    if (result is StateClass.Error) {
                        _errorMutable.emit(result.error)

                    } else if (result is StateClass.Success) {
                        _tweetPostedDataMutable.emit(TweetResponse(null))

                    }
                }
            }

            setNetworkLoader(View.GONE)

        }
    }
    fun clearEmpty(){ // intialize and remove the values with empty for second tweet so that there is no duplication found
        viewModelScope.launch {
            _tweetPostedDataMutable.emit(null) // remove listener
            _errorMutable.emit(null)
        }
    }

}