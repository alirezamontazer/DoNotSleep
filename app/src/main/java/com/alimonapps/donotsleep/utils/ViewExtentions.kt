package com.alimonapps.donotsleep.utils

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("isInvisible")
fun View.isInvisible(isInvisible: Boolean) {
    if (isInvisible) this.visibility = View.INVISIBLE else this.visibility = View.VISIBLE
}


