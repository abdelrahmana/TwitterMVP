package com.example.twitter.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {
    private val _networkLoader = MutableLiveData<Int?>() // visibilty
    val networkLoader :LiveData<Int?> = _networkLoader

    fun setNetworkLoader(loader : Int?) {
        _networkLoader.postValue(loader)
    }

}