package com.alimonapps.donotsleep.ui.login.step1

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alimonapps.donotsleep.utils.SharedPrefs

class RegisterStep1ViewModel(private val sharedPrefs: SharedPrefs) : ViewModel() {

    var phoneNumber = MutableLiveData("")
    val isShowLoading = MutableLiveData(false)


    fun savePhoneNumber() {
        sharedPrefs.saveMyPhoneNumber(phoneNumber = phoneNumber.value!!)
    }


}