package com.alimonapps.donotsleep.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alimonapps.donotsleep.R

/** Alireza Montazeralzohour
 * Login activity for holding login pages: step 1 fragment, step 2 fragment and base fragment
 */

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }
}