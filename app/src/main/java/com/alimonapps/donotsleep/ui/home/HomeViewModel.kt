package com.alimonapps.donotsleep.ui.home

import androidx.lifecycle.ViewModel
import com.alimonapps.donotsleep.utils.SharedPrefs

/** Alireza Montazeralzohour
 * Home view model loads the receiver phone number from the sharedPreferences.
 */

class HomeViewModel(private val sharedPrefs: SharedPrefs) : ViewModel() {

    fun loadPhoneNumber(): String {
        return sharedPrefs.loadFriendNumber()
    }

}