package com.alimonapps.donotsleep.utils

import android.view.View
import androidx.databinding.BindingAdapter

/** Alireza Montazeralzohour
 * In this app we implemented data binding technic. With the data binding in android development
 * we can create Binding adapters like this file and connect it directly to the XML file
 * and transfer data between the XML file and android files.
 * This file is used for loading animations in the app.
 */

@BindingAdapter("isInvisible")
fun View.isInvisible(isInvisible: Boolean) {
    if (isInvisible) this.visibility = View.INVISIBLE else this.visibility = View.VISIBLE
}


