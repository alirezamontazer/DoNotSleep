package com.alimonapps.donotsleep.di

import com.alimonapps.donotsleep.ui.MainViewModel
import com.alimonapps.donotsleep.ui.home.HomeViewModel
import com.alimonapps.donotsleep.ui.login.base.RegisterBaseViewModel
import com.alimonapps.donotsleep.ui.login.step1.RegisterStep1ViewModel
import com.alimonapps.donotsleep.ui.login.step2.RegisterStep2ViewModel
import com.alimonapps.donotsleep.ui.setting.SettingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    factory { provideMediaPlayer() }

}

val viewModelModule = module {

    viewModel { HomeViewModel() }
    viewModel { SettingViewModel() }
    viewModel { MainViewModel() }
    viewModel { RegisterBaseViewModel() }
    viewModel { RegisterStep1ViewModel() }
    viewModel { RegisterStep2ViewModel() }

}
