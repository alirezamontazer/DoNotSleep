package com.alimonapps.donotsleep

import androidx.lifecycle.ViewModel
import com.alimonapps.donotsleep.utils.SharedPrefs

class SplashViewModel(private val sharedPrefs: SharedPrefs) : ViewModel() {

    fun loadIsLoggedIn(): Boolean {
        return sharedPrefs.loadIsLoggedIn()
    }
}