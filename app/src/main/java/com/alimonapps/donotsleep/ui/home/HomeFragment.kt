package com.alimonapps.donotsleep.ui.home

import android.Manifest
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.alimonapps.donotsleep.Condition
import com.alimonapps.donotsleep.EyesTracker
import com.alimonapps.donotsleep.FaceTrackerDaemon
import com.alimonapps.donotsleep.databinding.HomeFragmentBinding
import com.alimonapps.donotsleep.ui.MainViewModel
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.MultiProcessor
import com.google.android.gms.vision.face.FaceDetector
import kotlinx.coroutines.*
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException

private const val TAG = "HomeFragment"

class HomeFragment : Fragment(), EyesTracker.OnChangeEyeExpression {

    private val viewModel: HomeViewModel by viewModel()
    private val sharedViewMode: MainViewModel by sharedViewModel()
    private val mMediaPlayer: MediaPlayer by inject()
    private var isMediaPlayerStarted = false
    private lateinit var binding: HomeFragmentBinding
    private lateinit var eyesTracker: EyesTracker
    private var myDisplayTime: Long = 500
    private var myRestTime: Long = 500


    private var flag = false
    private lateinit var cameraSource: CameraSource
    private val CAMERA_RQ = 101
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

        Log.e(TAG, "onCreate: on create oomad")

        clickOnStartButton()

        return binding.root

    }


    private fun clickOnStartButton() {
        binding.button2.setOnClickListener {
            requestPermission()
        }
    }

    private fun requestPermission() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_RQ
            )
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

    //Update view
    override fun onChangeEye(condition: Condition) {
        when (condition) {
            Condition.USER_EYES_OPEN -> {
                setBackgroundGreen()
                binding.userText.text = "Open eyes detected"
                if (this::myScope.isInitialized) myScope.cancel()
                if (isMediaPlayerStarted) mMediaPlayer.stop()
                counter.postValue(0)

            }
            Condition.USER_EYES_CLOSED -> {
                myScope = CoroutineScope(Dispatchers.Main)
                setBackgroundOrange()
                binding.userText.text = "Close eyes detected"

                counter.postValue(counter.value!!.plus(1))
                Log.e(TAG, "updateMainView: ${counter.value}")

                myScope.launch {
                    counter.observe(viewLifecycleOwner, object : Observer<Int> {
                        override fun onChanged(t: Int) {
                            if (t == 100) {
                                Toast.makeText(requireContext(), "WAKE UP!", Toast.LENGTH_SHORT)
                                    .show()
                                startMediaPlayer()
                            }
                            if (t >= 100) {
                                setBackgroundRed()
                                mMediaPlayer.isLooping = true
                            }
                            counter.removeObserver(this)
                        }
                    })
                }
            }
            Condition.FACE_NOT_FOUND -> {
                setBackgroundGrey()
                binding.userText.text = "User not found"
                if (this::myScope.isInitialized) myScope.cancel()
                if (isMediaPlayerStarted) mMediaPlayer.stop()
                counter.postValue(0)

            }
        }
    }

    private fun startMediaPlayer() {
        val voiceId: Int =
            resources.getIdentifier("alarm", "raw", requireContext().packageName)
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

    override fun onResume() {
        super.onResume()
        Log.e(TAG, "onResume: on resume oomad")
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

    override fun onStart() {
        super.onStart()
        Log.e(TAG, "onStart: on start oomad")
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
        if (this::cameraSource.isInitialized) cameraSource.stop()
        if (this::myScope.isInitialized) myScope.cancel()
        if (isMediaPlayerStarted) mMediaPlayer.stop()

    }

    override fun onPause() {
        super.onPause()
        if (this::cameraSource.isInitialized) cameraSource.stop()
        if (this::myScope.isInitialized) myScope.cancel()
        mMediaPlayer.stop()
    }

    override fun onDestroy() {
        super.onDestroy()

        if (this::cameraSource.isInitialized) cameraSource.release()
        if (this::cameraSource.isInitialized) cameraSource.stop()
        if (isMediaPlayerStarted) {
            mMediaPlayer.stop()
            mMediaPlayer.release()
        }
    }

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
            CAMERA_RQ -> innerCheck("camera")
        }
    }

}