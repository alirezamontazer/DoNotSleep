package com.alimonapps.donotsleep.ui.home

import androidx.lifecycle.ViewModel
import com.alimonapps.donotsleep.utils.SharedPrefs

class HomeViewModel(private val sharedPrefs: SharedPrefs) : ViewModel() {

    fun loadPhoneNumber(): String {
        return sharedPrefs.loadFriendNumber()
    }

}