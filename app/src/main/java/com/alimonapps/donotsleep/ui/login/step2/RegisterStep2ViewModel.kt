package com.alimonapps.donotsleep.ui.login.step2

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.alimonapps.donotsleep.utils.SharedPrefs

/**
 * Alireza Montazeralzohour - Mohsen Shahbazi
 * Step 2 registration page view model which saves the receiver phone number and isUserLoggedIn
 * using sharedPreferences
 */

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