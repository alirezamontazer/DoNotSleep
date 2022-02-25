package com.alimonapps.donotsleep

import android.app.Application
import com.alimonapps.donotsleep.di.appModule
import com.alimonapps.donotsleep.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level


class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // start Koin!
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@BaseApplication)
            modules(listOf(appModule, viewModelModule))
        }

    }
}