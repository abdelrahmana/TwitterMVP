package com.example.twitter.data.model

sealed class StateClass(){
    class Loading() : StateClass()
    class Success(): StateClass()
    class Error(val error : String) : StateClass()
}