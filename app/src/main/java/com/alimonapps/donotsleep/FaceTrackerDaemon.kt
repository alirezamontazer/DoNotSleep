package com.alimonapps.donotsleep

import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.Tracker
import com.google.android.gms.vision.face.Face

/** Alireza Montazeralzohour
 * This file provides background thread processing for EyesTracker class
 * which it means it does not block the main thread of the app.
 * MultiProcessor is provided by Google Cloud Vision API.
 */

class FaceTrackerDaemon(private val onChangeEyeExpression: EyesTracker.OnChangeEyeExpression) :
    MultiProcessor.Factory<Face> {

    override fun create(face: Face?): Tracker<Face> {
        return EyesTracker(onChangeEyeExpression)
    }

}