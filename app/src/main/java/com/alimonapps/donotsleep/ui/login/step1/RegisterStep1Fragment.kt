package com.alimonapps.donotsleep.ui.login.step1

import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.alimonapps.donotsleep.databinding.RegisterStep1FragmentBinding
import com.alimonapps.donotsleep.ui.login.base.RegisterBaseViewModel
import com.alimonapps.donotsleep.utils.errorToast
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val TAG = "RegisterStep1Fragment"

class RegisterStep1Fragment : Fragment() {

    private val viewModel: RegisterStep1ViewModel by viewModel()
    private val sharedViewModel: RegisterBaseViewModel by sharedViewModel()
    private lateinit var binding: RegisterStep1FragmentBinding
    private lateinit var countDownTimer: CountDownTimer


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RegisterStep1FragmentBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        clickOnRegister()

        return binding.root
    }

    private fun clickOnRegister() {
        binding.btnReceive.setOnClickListener {
            checkTextValidity()
            Log.e(TAG, "clickOnRegister: ${viewModel.phoneNumber.value}")
            if (checkTextValidity()) {
                viewModel.isShowLoading.value = true
                viewModel.savePhoneNumber()
                goToNextPage()
            }
        }
    }

    private fun checkTextValidity(): Boolean {
        val isTextCorrect: Boolean

        if (viewModel.phoneNumber.value == "" || viewModel.phoneNumber.value!!.length < 10) {
            isTextCorrect = false
            errorToast("Phone number is not valid")
        } else isTextCorrect = true

        return isTextCorrect
    }

    private fun goToNextPage() {
        countDownTimer = object : CountDownTimer(3000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                Log.e(TAG, "onFinish: go to next page")
                sharedViewModel.openViewPagerFragmentNumber.value = 1
                Log.e(TAG, "${sharedViewModel.openViewPagerFragmentNumber.value}")
            }
        }
        countDownTimer.start()
    }


}