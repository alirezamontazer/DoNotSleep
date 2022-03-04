package com.alimonapps.donotsleep.di

import android.media.MediaPlayer
import android.telephony.SmsManager

fun provideMediaPlayer(): MediaPlayer {
    return MediaPlayer()
}

fun provideSmsManager(): SmsManager {
    return SmsManager.getDefault()
}