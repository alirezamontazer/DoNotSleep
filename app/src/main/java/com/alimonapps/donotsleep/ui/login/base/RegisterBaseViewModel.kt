package com.alimonapps.donotsleep.ui.login.base

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Alireza Montazeralzohour - Mohsen Shahbazi
 * Resister base view model which is a shared view model between register base and
 * step 1 and step 2 to transfer data
 */

class RegisterBaseViewModel : ViewModel() {

    val openViewPagerFragmentNumber = MutableLiveData(-1)
    val openMainActivity = MutableLiveData(false)

}