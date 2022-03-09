package com.alimonapps.donotsleep.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.telephony.SmsManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.alimonapps.donotsleep.Condition
import com.alimonapps.donotsleep.EyesTracker
import com.alimonapps.donotsleep.FaceTrackerDaemon
import com.alimonapps.donotsleep.R
import com.alimonapps.donotsleep.databinding.HomeFragmentBinding
import com.alimonapps.donotsleep.ui.MainViewModel
import com.alimonapps.donotsleep.utils.errorToast
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.face.FaceDetector
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException

/** Alireza Montazeralzohour
 * This is the main page that gets the data from FaceTrackerDaemon and displays
 * that data on the phone's screen. In this file first we get the user's permissions
 * for accessing camera and sending text messages. Next initializing the face detector
 * with FaceTrackerDaemon and camera source. We used Android Coroutines in order to handle
 * the data from background thread and show on UI thread. Also we used MutableLiveData class
 * for calculating the time that user closed his or her eyes.
 */

private const val TAG = "HomeFragment"

class HomeFragment : Fragment(), EyesTracker.OnChangeEyeExpression {

    private val viewModel: HomeViewModel by viewModel()
    private val sharedViewMode: MainViewModel by sharedViewModel()
    private val mMediaPlayer: MediaPlayer by inject()
    private val smsManager: SmsManager by inject()
    private var isMediaPlayerStarted = false
    private lateinit var binding: HomeFragmentBinding
    private lateinit var eyesTracker: EyesTracker
    private var friendPhoneNumber: String = ""
    private var isAlarmSet = false
    private var isOpenEyes = false
    private var isNoFace = false
    private var isCloseEyes = false

    private var flag = false
    private lateinit var cameraSource: CameraSource
    private val PERMISSION_RQ_CODE = 101
    private lateinit var myScope: CoroutineScope
    var counter = MutableLiveData(0)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = HomeFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        eyesTracker = EyesTracker(this)

        loadPhoneNumbers()
        clickOnStartButton()

        return binding.root

    }

    // Loading Receiver phone number from sharedPreferences
    private fun loadPhoneNumbers() {
        friendPhoneNumber = viewModel.loadPhoneNumber()
        Log.e(TAG, "loadPhoneNumbers: $friendPhoneNumber")
    }


    // By clicking on start button the app starts working
    private fun clickOnStartButton() {
        binding.button2.setOnClickListener {
            requestPermission()
            binding.loadingLottie.visibility = View.VISIBLE
        }
    }

    // Requesting camera and send and receive sms permission from the user
    private fun requestPermission() {
        if (!checkPermissionFromDevice()) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.SEND_SMS,
                    Manifest.permission.RECEIVE_SMS
                ),
                PERMISSION_RQ_CODE
            )
        } else {
            initApp()
        }
    }

    // Checking permissions to see the user permitted for accessing the camera and sending sms
    private fun checkPermissionFromDevice(): Boolean {

        val cameraPermissionResult = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        )

        val sendSmsResult = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.SEND_SMS
        )

        val receiveSmsResult = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.RECEIVE_SMS
        )

        return cameraPermissionResult == PackageManager.PERMISSION_GRANTED
                && sendSmsResult == PackageManager.PERMISSION_GRANTED
                && receiveSmsResult == PackageManager.PERMISSION_GRANTED

    }

    // if the permissions granted, the face tracking can start working
    private fun initApp() {
        flag = true
        initCameraSource()
    }

    //Method to create camera source from faceFactoryDaemon class
    private fun initCameraSource() {
        val detector = FaceDetector.Builder(requireContext()).setTrackingEnabled(true)

            .setClassificationType(FaceDetector.ALL_CLASSIFICATIONS).setMode(FaceDetector.FAST_MODE)
            .build()

        detector.setProcessor(
            MultiProcessor.Builder(FaceTrackerDaemon(this)).build()
        )


        cameraSource = CameraSource.Builder(requireContext(), detector)
            .setRequestedPreviewSize(1024, 768)
            .setFacing(CameraSource.CAMERA_FACING_FRONT)
            .setRequestedFps(30.0f)
            .build()

        try {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermission()
                return
            }
            cameraSource.start()
            cameraSource.previewSize
        } catch (e: IOException) {
            Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }
    }

    // This is the interface that gets the data from EyeTracker class and handles the data
    override fun onChangeEye(condition: Condition) {
        myScope = CoroutineScope(Dispatchers.Main)

        when (condition) {

            // OPEN EYES
            Condition.USER_EYES_OPEN -> {
                isAlarmSet = false
                isNoFace = false
                isCloseEyes = false
                if (!isOpenEyes) openEyesVisibility()
                setBackgroundGreen()

                if (this::myScope.isInitialized) myScope.cancel()
                if (isMediaPlayerStarted) mMediaPlayer.stop()
                counter.postValue(0)
            }

            // CLOSED EYES
            Condition.USER_EYES_CLOSED -> {
                isNoFace = false
                isOpenEyes = false
                if (!isCloseEyes) closeEyesVisibility()
                setBackgroundOrange()

                counter.postValue(counter.value!!.plus(1))
                Log.e(TAG, "updateMainView: ${counter.value}")

                myScope.launch {
                    counter.observe(viewLifecycleOwner, object : Observer<Int> {
                        override fun onChanged(t: Int) {
                            if (t == 100) {
                                errorToast("WAKE UP !!!")
                                startMediaPlayer()
                            }
                            if (t >= 100) {
                                setBackgroundRed()
                                mMediaPlayer.isLooping = true
                                if (!isAlarmSet) {
                                    alertIconVisibility()
                                    sendSms()
                                }
                            }
                            counter.removeObserver(this)
                        }
                    })
                }
            }

            // FACE NOT DETECTED
            Condition.FACE_NOT_FOUND -> {
                isAlarmSet = false
                isCloseEyes = false
                isOpenEyes = false
                if (!isNoFace) noFaceVisibility()
                setBackgroundGrey()
                if (this::myScope.isInitialized) myScope.cancel()
                if (isMediaPlayerStarted) mMediaPlayer.stop()
                counter.postValue(0)

            }
        }
    }

    // Sound alarm
    private fun startMediaPlayer() {
        val voiceId: Int =
            resources.getIdentifier("alarm2", "raw", requireContext().packageName)
        val voicePath =
            Uri.parse("android.resource://com.alimonapps.donotsleep/$voiceId")
        try {
            mMediaPlayer.reset()
            mMediaPlayer.setDataSource(requireContext(), voicePath)
            mMediaPlayer.prepare()

        } catch (e: IllegalStateException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        }
        mMediaPlayer.start()
        isMediaPlayerStarted = true
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

    // Changing the icons on the screen if eyes are open
    private fun openEyesVisibility() {
        activity?.runOnUiThread {
            binding.tvDescription.text = getString(R.string.opened_eyes)
            binding.imgCheck.visibility = View.VISIBLE
            binding.imgNotFound.visibility = View.INVISIBLE
            binding.imgWarning.visibility = View.INVISIBLE
            binding.imgDanger.visibility = View.INVISIBLE
            isOpenEyes = true
        }
    }

    // Changing the icons on the screen if eyes are closed
    private fun closeEyesVisibility() {
        activity?.runOnUiThread {
            binding.tvDescription.text = getString(R.string.closed_eyes)
            binding.imgWarning.visibility = View.VISIBLE
            binding.imgCheck.visibility = View.INVISIBLE
            binding.imgNotFound.visibility = View.INVISIBLE
            binding.imgDanger.visibility = View.INVISIBLE
            isCloseEyes = true
        }
    }

    // Changing the icons on the screen if eyes are closed for a long time
    private fun alertIconVisibility() {
        activity?.runOnUiThread {
            binding.imgDanger.visibility = View.VISIBLE
            binding.imgWarning.visibility = View.INVISIBLE
            binding.imgCheck.visibility = View.INVISIBLE
            binding.imgNotFound.visibility = View.INVISIBLE
            isAlarmSet = true
        }
    }

    // Sending Sms
    private fun sendSms() {
        try {
            smsManager.sendTextMessage(
                friendPhoneNumber,
                null,
                "HELP ME !!!",
                null,
                null
            )
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    // Changing the icons on the screen if user not detected
    private fun noFaceVisibility() {
        activity?.runOnUiThread {
            binding.tvDescription.text = getString(R.string.user_not_detected)
            binding.imgNotFound.visibility = View.VISIBLE
            binding.imgCheck.visibility = View.INVISIBLE
            binding.imgWarning.visibility = View.INVISIBLE
            binding.imgDanger.visibility = View.INVISIBLE
            isNoFace = true
        }
    }


    // onResume called when the app starts working after a pause
    override fun onResume() {
        super.onResume()
        try {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermission()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    // onStart called when the app starts
    override fun onStart() {
        super.onStart()
        try {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.CAMERA
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermission()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onStop() {
        super.onStop()
        pauseApp()

    }

    override fun onPause() {
        super.onPause()
        pauseApp()
    }


    // onDestroy called when the app ends
    override fun onDestroy() {
        super.onDestroy()
        if (this::cameraSource.isInitialized) cameraSource.release()
        if (this::cameraSource.isInitialized) cameraSource.stop()
        if (isMediaPlayerStarted) {
            mMediaPlayer.stop()
            mMediaPlayer.release()
        }
    }

    private fun pauseApp() {
        setBackgroundGrey()
        noFaceVisibility()
        binding.loadingLottie.visibility = View.INVISIBLE
        binding.tvDescription.text = getString(R.string.app_start)
        if (this::cameraSource.isInitialized) cameraSource.stop()
        if (this::myScope.isInitialized) myScope.cancel()
        if (isMediaPlayerStarted) mMediaPlayer.stop()
    }

    // After getting permissions the app continuous working
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        fun innerCheck(name: String) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(requireContext(), "$name permission refused", Toast.LENGTH_SHORT)
                    .show()
            } else {
                initApp()
            }
        }

        when (requestCode) {
            PERMISSION_RQ_CODE -> innerCheck("camera")
        }
    }

}