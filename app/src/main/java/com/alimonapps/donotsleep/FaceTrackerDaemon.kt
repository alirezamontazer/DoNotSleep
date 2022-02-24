package com.alimonapps.donotsleep

import android.content.Context
import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.Tracker
import com.google.android.gms.vision.face.Face

class FaceTrackerDaemon(private val context: Context) : MultiProcessor.Factory<Face> {

    override fun create(face: Face?): Tracker<Face> {
        return EyesTracker(context)
    }
}