package com.alimonapps.donotsleep

import android.content.Context
import android.util.Log
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Tracker
import com.google.android.gms.vision.face.Face

private const val TAG = "EyesTracker"

class EyesTracker(private val context: Context) : Tracker<Face>() {
    private val THRESHOLD = 0.75f


    override fun onUpdate(detections: Detector.Detections<Face>?, face: Face?) {
        if (face!!.isLeftEyeOpenProbability > THRESHOLD || face.isRightEyeOpenProbability > THRESHOLD) {
            Log.i(TAG, "onUpdate: Open Eyes Detected")
            (context as MainActivity).updateMainView(Condition.USER_EYES_OPEN)
        } else {
            Log.i(TAG, "onUpdate: Close Eyes Detected");
            (context as MainActivity).updateMainView(Condition.USER_EYES_CLOSED)
        }
    }

    override fun onMissing(detections: Detector.Detections<Face>?) {
        super.onMissing(detections)
        Log.i(TAG, "onUpdate: Face Not Detected yet!");
        (context as MainActivity).updateMainView(Condition.FACE_NOT_FOUND)
    }

    override fun onDone() {
        super.onDone()
    }
}