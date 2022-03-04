package com.alimonapps.donotsleep.ui.login.step2

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alimonapps.donotsleep.utils.SharedPrefs

class RegisterStep2ViewModel(private val sharedPrefs: SharedPrefs) : ViewModel() {

    var phoneNumberEmergency = MutableLiveData("")
    val isShowLoading = MutableLiveData(false)

    fun savePhoneNumber() {
        sharedPrefs.saveFriendPhoneNumber(phoneNumber = phoneNumberEmergency.value!!)
    }

    fun saveLoggedIn(isLoggedIn: Boolean) {
        sharedPrefs.saveIsLoggedIn(isLoggedIn)
    }

}