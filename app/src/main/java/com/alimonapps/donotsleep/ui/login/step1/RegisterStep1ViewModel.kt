package com.alimonapps.donotsleep.ui.login.step1

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterStep1ViewModel : ViewModel() {

    var phoneNumber = MutableLiveData("")
    val isShowLoading = MutableLiveData(false)


}