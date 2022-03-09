package com.alimonapps.donotsleep.utils

import android.content.SharedPreferences

/** Alireza Montazeralzohour - Mohsen Shahbazi
 * This file provides sharedPreferences in the app which we can save the information
 * that user puts in such as phone numbers and is user logged in or not.
 */

class SharedPrefs(private val mPreferences: SharedPreferences) {

    companion object {
        private const val MY_DATA = "my data"
        private const val FRIEND_DATA = "friend data"
        private const val LOGIN_DATA = "login data"
    }

    fun saveIsLoggedIn(isLoggedIn: Boolean) {
        mPreferences.edit().putBoolean(LOGIN_DATA, isLoggedIn).apply()

    }

    fun loadIsLoggedIn(): Boolean {
        return mPreferences.getBoolean(LOGIN_DATA, false)
    }

    fun saveMyPhoneNumber(phoneNumber: String) {
        putString(MY_DATA, phoneNumber)
    }

    fun saveFriendPhoneNumber(phoneNumber: String) {
        putString(FRIEND_DATA, phoneNumber)
    }

    fun loadMyPhoneNumber(): String {
        return getString(MY_DATA)
    }

    fun loadFriendNumber(): String {
        return getString(FRIEND_DATA)
    }


    private fun getString(key: String, default: String = ""): String {
        return mPreferences.getString(key, null) ?: default
    }

    private fun putString(key: String, value: String) {
        mPreferences.edit().putString(key, value).apply()
    }

}