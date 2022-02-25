package com.alimonapps.donotsleep.di

import com.alimonapps.donotsleep.ui.MainViewModel
import com.alimonapps.donotsleep.ui.home.HomeViewModel
import com.alimonapps.donotsleep.ui.setting.SettingViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {}

val viewModelModule = module() {

    viewModel { HomeViewModel() }
    viewModel { SettingViewModel() }
    viewModel { MainViewModel() }

}
