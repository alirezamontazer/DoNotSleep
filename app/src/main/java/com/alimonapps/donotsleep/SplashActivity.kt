package com.alimonapps.donotsleep

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.alimonapps.donotsleep.ui.MainActivity
import com.alimonapps.donotsleep.ui.login.LoginActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        setupCountDownTimer()

    }

    private fun setupCountDownTimer() {
        val timer = object : CountDownTimer(4000, 1000) {
            override fun onTick(p0: Long) {
            }

            override fun onFinish() {
                val intent = Intent(applicationContext, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
            }

        }
        timer.start()
    }

}