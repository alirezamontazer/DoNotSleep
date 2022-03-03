package com.alimonapps.donotsleep.ui.login.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RegisterBaseViewModel : ViewModel() {

    val openViewPagerFragmentNumber = MutableLiveData(-1)
    val openMainActivity = MutableLiveData(false)

}