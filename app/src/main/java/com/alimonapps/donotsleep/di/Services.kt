package com.alimonapps.donotsleep.di

import android.media.MediaPlayer
import android.telephony.SmsManager

/** Alireza Montazeralzohour
 * providing media player to koin dependency injection to be injected
 */

fun provideMediaPlayer(): MediaPlayer {
    return MediaPlayer()
}

fun provideSmsManager(): SmsManager {
    return SmsManager.getDefault()
}