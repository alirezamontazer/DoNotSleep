package com.alimonapps.donotsleep

import android.util.Log
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.Tracker
import com.google.android.gms.vision.face.Face

/** Alireza Montazeralzohour
 * This file is the eye tracker which is provided by Google Cloud Vision API.
 * It detects the face and different eyes expression.
 * It has 3 different states: eyes open, closed and user not detected.
 * and sends the data with onChangeEyeExpression interface to home fragment.
 */

private const val TAG = "EyesTracker"

class EyesTracker(private val onChangeEyeExpression: OnChangeEyeExpression) : Tracker<Face>() {

    private val THRESHOLD = 0.75f

    override fun onUpdate(detections: Detector.Detections<Face>?, face: Face?) {
        if (face!!.isLeftEyeOpenProbability > THRESHOLD || face.isRightEyeOpenProbability > THRESHOLD) {
            Log.i(TAG, "onUpdate: Open Eyes Detected")
            onChangeEyeExpression.onChangeEye(Condition.USER_EYES_OPEN)
        } else {
            Log.i(TAG, "onUpdate: Close Eyes Detected");
            onChangeEyeExpression.onChangeEye(Condition.USER_EYES_CLOSED)
        }
    }

    override fun onMissing(detections: Detector.Detections<Face>?) {
        super.onMissing(detections)
        Log.i(TAG, "onUpdate: Face Not Detected yet!");
        onChangeEyeExpression.onChangeEye(Condition.FACE_NOT_FOUND)
    }

    interface OnChangeEyeExpression {
        fun onChangeEye(condition: Condition)
    }

}