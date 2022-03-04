package com.alimonapps.donotsleep

import android.app.Application
import android.content.Context
import com.alimonapps.donotsleep.di.appModule
import com.alimonapps.donotsleep.di.storeModules
import com.alimonapps.donotsleep.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level


class BaseApplication : Application() {

    companion object {
        @get:Synchronized
        var instance: BaseApplication? = null

        fun applicationContext(): Context {
            return instance!!.applicationContext
        }

    }

    override fun onCreate() {
        super.onCreate()
        instance = this

        // start Koin!
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@BaseApplication)
            modules(listOf(appModule, storeModules, viewModelModule))
        }

    }
}