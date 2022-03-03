package com.alimonapps.donotsleep.utils

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Build
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getColor
import androidx.core.internal.view.SupportMenu
import com.alimonapps.donotsleep.BaseApplication
import com.alimonapps.donotsleep.BaseApplication.Companion.applicationContext
import com.alimonapps.donotsleep.R

var WIDTH = -1

fun successToast(msg: String) {
    customToast(msg, getColor(applicationContext(), R.color.bg_toast_success))
}

fun errorToast(msg: String) {
    customToast(msg, getColor(applicationContext(), R.color.bg_toast_error))
}

private fun customToast(msg: String, backgroundColor: Int) {
    val layout = LinearLayout(BaseApplication.instance)
    layout.setBackgroundColor(backgroundColor)
    val tv = TextView(BaseApplication.instance)
    tv.setTextColor(getColor(applicationContext(), R.color.white))
    tv.textSize = 13f
    tv.gravity = 16
    tv.text = msg
    tv.gravity = 17
    tv.maxWidth = screenWidth() - 110
    tv.setTextColor(Color.WHITE)
    layout.addView(tv)
    layout.setPadding(35, 25, 35, 25)
    val gradientDrawable = GradientDrawable(
        GradientDrawable.Orientation.TOP_BOTTOM,
        intArrayOf(backgroundColor, backgroundColor)
    )
    gradientDrawable.cornerRadius = 16f
    if (Build.VERSION.SDK_INT >= 17) {
        layout.background = gradientDrawable
    } else {
        layout.setBackgroundDrawable(gradientDrawable)
    }
    val toast = Toast(BaseApplication.instance)
    toast.view = layout
    toast.setGravity(80, 0, 100)
    toast.duration = Toast.LENGTH_SHORT
    toast.show()
}

fun screenWidth(): Int {
    if (WIDTH == -1) {
        WIDTH = BaseApplication.instance?.resources?.displayMetrics!!.widthPixels
    }
    return WIDTH
}
