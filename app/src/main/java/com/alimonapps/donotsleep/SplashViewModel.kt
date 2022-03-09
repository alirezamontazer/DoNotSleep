package com.alimonapps.donotsleep

import androidx.lifecycle.ViewModel
import com.alimonapps.donotsleep.utils.SharedPrefs

/** Mohsen Shahbazi
 * This file is Splash activity view model which loads the information about the user
 * that is logged in or not
 */

class SplashViewModel(private val sharedPrefs: SharedPrefs) : ViewModel() {

    fun loadIsLoggedIn(): Boolean {
        return sharedPrefs.loadIsLoggedIn()
    }
}