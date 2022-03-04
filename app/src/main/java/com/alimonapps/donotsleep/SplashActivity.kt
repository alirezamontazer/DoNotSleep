package com.alimonapps.donotsleep

import android.content.Intent
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.os.CountDownTimer
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.model.KeyPath
import com.airbnb.lottie.value.SimpleLottieValueCallback
import com.alimonapps.donotsleep.databinding.ActivitySplashBinding
import com.alimonapps.donotsleep.ui.MainActivity
import com.alimonapps.donotsleep.ui.login.LoginActivity
import org.koin.androidx.viewmodel.ext.android.viewModel

class SplashActivity : AppCompatActivity() {

    private val viewModel: SplashViewModel by viewModel()
    private lateinit var binding: ActivitySplashBinding
    private var isLoggedIn = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_splash)
        binding.lifecycleOwner = this

        loadIsLoggedIn()
        setupCountDownTimer()

    }

    private fun loadIsLoggedIn() {
        isLoggedIn = viewModel.loadIsLoggedIn()
    }

    private fun setupCountDownTimer() {
        val timer = object : CountDownTimer(4000, 1000) {
            override fun onTick(p0: Long) {
            }

            override fun onFinish() {
                if (isLoggedIn) goToMainPage()
                else goToLoginPage()
            }

        }
        timer.start()
    }

    private fun goToLoginPage() {
        val intent = Intent(applicationContext, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    private fun goToMainPage() {
        val intent = Intent(applicationContext, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

}