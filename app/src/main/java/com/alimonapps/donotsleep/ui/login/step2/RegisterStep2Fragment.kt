package com.alimonapps.donotsleep.ui.login.step2

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alimonapps.donotsleep.databinding.RegisterStep2FragmentBinding
import com.alimonapps.donotsleep.ui.login.base.RegisterBaseViewModel
import com.alimonapps.donotsleep.utils.errorToast
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * Alireza Montazeralzohour - Mohsen Shahbazi
 * Step 2 registration page which accepts the receiver's phone number
 */

private const val TAG = "RegisterStep2Fragment"

class RegisterStep2Fragment : Fragment() {

    private val viewModel: RegisterStep2ViewModel by viewModel()
    private val sharedViewModel: RegisterBaseViewModel by sharedViewModel()
    private lateinit var binding: RegisterStep2FragmentBinding
    private lateinit var countDownTimer: CountDownTimer


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RegisterStep2FragmentBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        clickOnRegister()

        return binding.root
    }

    // Click on register button
    private fun clickOnRegister() {
        binding.btnRegister.setOnClickListener {
            checkTextValidity()
            Log.e(TAG, "step2 number: ${viewModel.phoneNumberEmergency.value}")
            if (checkTextValidity()) {
                viewModel.isShowLoading.value = true
                viewModel.savePhoneNumber()
                viewModel.saveLoggedIn(true)
                goToNextPage()
            }

        }
    }

    // Check validity of the phone number
    private fun checkTextValidity(): Boolean {
        val isTextCorrect: Boolean

        if (viewModel.phoneNumberEmergency.value == "" || viewModel.phoneNumberEmergency.value!!.length < 10) {
            isTextCorrect = false
            errorToast("Phone number is not valid")
        } else isTextCorrect = true

        return isTextCorrect
    }


    // If the phone number is correct, shows the confirmation dialog
    private fun goToNextPage() {
        countDownTimer = object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
                Log.e(TAG, "onFinish: go to next page")
                gotoDialogFragment()
            }
        }
        countDownTimer.start()
    }

    private fun gotoDialogFragment() {
        sharedViewModel.openMainActivity.value = true
    }

}