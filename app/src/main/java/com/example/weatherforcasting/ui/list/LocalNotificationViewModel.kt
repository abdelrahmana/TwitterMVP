package com.example.weatherforcasting.ui.list

import android.view.View
import androidx.lifecycle.viewModelScope
import com.example.weatherforcasting.base.BaseViewModel
import com.example.weatherforcasting.data.cititesrepo.LocalNotification
import com.skydoves.sandwich.onError
import com.skydoves.sandwich.onSuccess
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class LocalNotificationViewModel @Inject constructor(
    private val weatherRepo: LocalNotification
) :
    BaseViewModel() {
    private val _errorMutable = MutableStateFlow<String?>(null)
    val errorStateFlow: StateFlow<String?> = _errorMutable
    private val _notificationDataMutable = MutableStateFlow<String?>(null)
    val notificationDataStateFlow: StateFlow<String?> = _notificationDataMutable
    fun getNotificationData(
    ) {
        setNetworkLoader(View.VISIBLE)
        viewModelScope.launch(Dispatchers.IO) {
            val result = weatherRepo.getLocalNotification()
            setNetworkLoader(View.GONE)
            with(result) {
                this@with.onSuccess {
                    viewModelScope.launch(Dispatchers.IO) {
                        _notificationDataMutable.emit(data)
                    }
                }

                this@with.onError {
                    viewModelScope.launch(Dispatchers.IO) {
                        _errorMutable.emit(errorBody.toString())
                    }
                }
                     // to cover the case of offline

               /* error?.let { it ->
                    setError(it)
                }*/
            }
        }
    }

}