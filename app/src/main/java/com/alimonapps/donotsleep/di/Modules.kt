package com.alimonapps.donotsleep.di

import com.alimonapps.donotsleep.SplashViewModel
import com.alimonapps.donotsleep.ui.MainViewModel
import com.alimonapps.donotsleep.ui.home.HomeViewModel
import com.alimonapps.donotsleep.ui.login.base.RegisterBaseViewModel
import com.alimonapps.donotsleep.ui.login.step1.RegisterStep1ViewModel
import com.alimonapps.donotsleep.ui.login.step2.RegisterStep2ViewModel
import com.alimonapps.donotsleep.ui.setting.SettingViewModel
import com.alimonapps.donotsleep.utils.provideSharedPreferences
import com.alimonapps.donotsleep.utils.provideSharedPrefs
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

/** Alireza Montazeralzohour
 * Providing modules for Koin dependency injection
 */

val appModule = module {
    factory { provideMediaPlayer() }
    single { provideSmsManager() }
}

val storeModules = module {
    single { provideSharedPreferences(get()) }
    single { provideSharedPrefs(get()) }
}

val viewModelModule = module {

    viewModel { HomeViewModel(get()) }
    viewModel { SettingViewModel() }
    viewModel { MainViewModel() }
    viewModel { SplashViewModel(get()) }
    viewModel { RegisterBaseViewModel() }
    viewModel { RegisterStep1ViewModel(get()) }
    viewModel { RegisterStep2ViewModel(get()) }

}
