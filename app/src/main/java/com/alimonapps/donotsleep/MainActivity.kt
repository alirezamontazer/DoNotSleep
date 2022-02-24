package com.alimonapps.donotsleep

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.alimonapps.donotsleep.databinding.ActivityMainBinding
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.face.FaceDetector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import java.io.IOException

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    var flag = false
    lateinit var cameraSource: CameraSource
    val CAMERA_RQ = 101
    private lateinit var countDownTimer: CountDownTimer
    private lateinit var myScope: CoroutineScope
    var seconds: Long = 0L
    var counter = MutableLiveData(0)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        requestPermission()

    }

    private fun requestPermission() {
        if (ActivityCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_RQ)
            Toast.makeText(
                this,
                "Permission not granted!\n Grant permission and restart app",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            initApp()
        }
    }

    private fun initApp() {
        flag = true
        initCameraSource()

    }

    //Method to create camera source from faceFactoryDaemon class
    private fun initCameraSource() {
        val detector = FaceDetector.Builder(this).setTrackingEnabled(true)

            .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS).setMode(FaceDetector.FAST_MODE)
            .build()

        detector.setProcessor(
            MultiProcessor.Builder(FaceTrackerDaemon(this@MainActivity)).build()
        )

        cameraSource = CameraSource.Builder(this, detector)
            .setRequestedPreviewSize(1024, 768)
            .setFacing(CameraSource.CAMERA_FACING_FRONT)
            .setRequestedFps(30.0f)
            .build()

        try {
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermission()
                return
            }
            cameraSource.start();
        } catch (e: IOException) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    //Update view
    fun updateMainView(condition: Condition) {
        when (condition) {
            Condition.USER_EYES_OPEN -> {
                setBackgroundGreen()
                binding.userText.text = "Open eyes detected"
                if (this::countDownTimer.isInitialized) countDownTimer.cancel()
                if (this::myScope.isInitialized) myScope.cancel()
                counter.postValue(0)

            }
            Condition.USER_EYES_CLOSED -> {
                myScope = CoroutineScope(Dispatchers.Main)
                setBackgroundOrange()
                binding.userText.text = "Close eyes detected"

                counter.postValue(counter.value!!.plus(1))
                Log.e(TAG, "updateMainView: ${counter.value}")

                myScope.launch {
                    counter.observe(this@MainActivity, object : Observer<Int> {
                        override fun onChanged(t: Int?) {
                            if (t == 200) {
                                Toast.makeText(applicationContext, "WAKE UP!", Toast.LENGTH_SHORT)
                                    .show()
                            }
                            counter.removeObserver(this)
                        }

                    })
                }
            }
            Condition.FACE_NOT_FOUND -> {
                setBackgroundRed()
                binding.userText.text = "User not found"
                if (this::countDownTimer.isInitialized) countDownTimer.cancel()
                if (this::myScope.isInitialized) myScope.cancel()
                counter.postValue(0)

            }
        }

    }

    private fun setupCountDownTimer(countDownTime: Long) {
        countDownTimer = object : CountDownTimer(countDownTime, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                seconds = millisUntilFinished / 1000

            }

            override fun onFinish() {
                Log.e(TAG, "onFinish: $seconds")
                if (seconds.toInt() == 5) {
                    Toast.makeText(applicationContext, "wake up", Toast.LENGTH_SHORT).show()
                } else return
            }
        }
        countDownTimer.start()
    }

    private fun setBackgroundGrey() {
        binding.background.setBackgroundColor(resources.getColor(android.R.color.darker_gray))
    }

    private fun setBackgroundRed() {
        binding.background.setBackgroundColor(resources.getColor(android.R.color.holo_red_dark))
    }

    private fun setBackgroundOrange() {
        binding.background.setBackgroundColor(resources.getColor(android.R.color.holo_orange_dark))
    }

    private fun setBackgroundGreen() {
        binding.background.setBackgroundColor(resources.getColor(android.R.color.holo_green_dark))
    }

    override fun onResume() {
        super.onResume()
        try {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermission()
                return
            }
            cameraSource.start()
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    override fun onPause() {
        super.onPause()
        if (this::cameraSource.isInitialized) {
            cameraSource.stop()
        }
        setBackgroundGrey()
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraSource.release()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        fun innerCheck(name: String) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(applicationContext, "$name permission refused", Toast.LENGTH_SHORT)
                    .show()
            } else {
                initApp()
            }
        }

        when (requestCode) {
            CAMERA_RQ -> innerCheck("camera")
        }
    }
}
