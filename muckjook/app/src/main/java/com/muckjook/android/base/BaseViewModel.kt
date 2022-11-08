package com.muckjook.android.base

import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.muckjook.android.widget.utils.ScreenState
import com.muckjook.android.widget.utils.SingleLiveEvent
import com.muckjook.domain.utils.ErrorType
import com.muckjook.domain.utils.RemoteErrorEmitter

abstract class BaseViewModel : ViewModel(), RemoteErrorEmitter {

    val mutableProgress = MutableLiveData<Int>(View.GONE)
    val mutableScreenState = SingleLiveEvent<ScreenState>()
    val mutableErrorMessage = SingleLiveEvent<String>()
    val mutableSuccessMessage = MutableLiveData<String>()
    val mutableErrorType = SingleLiveEvent<ErrorType>()



}