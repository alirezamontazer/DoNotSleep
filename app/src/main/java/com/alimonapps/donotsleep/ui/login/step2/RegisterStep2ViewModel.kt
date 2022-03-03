package com.alimonapps.donotsleep.ui.login.step2

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterStep2ViewModel : ViewModel() {

    var phoneNumberEmergency = MutableLiveData("")
    val isShowLoading = MutableLiveData(false)


}