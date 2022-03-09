package com.alimonapps.donotsleep.utils

import android.content.Context
import android.content.SharedPreferences
import com.alimonapps.donotsleep.PREF_NAME

/** Mohsen Shahbazi
 * This file provides SharedPreferences instance for Koin dependency injection
 */

fun provideSharedPreferences(context: Context): SharedPreferences? {
    return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
}

fun provideSharedPrefs(sharedPreferences: SharedPreferences): SharedPrefs {
    return SharedPrefs(sharedPreferences)
}