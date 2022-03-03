package com.alimonapps.donotsleep.ui.login.confirmdialog

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.DialogFragment
import com.alimonapps.donotsleep.R
import com.alimonapps.donotsleep.databinding.FragmentConfirmDialogBinding
import com.alimonapps.donotsleep.ui.MainActivity
import com.alimonapps.donotsleep.ui.login.LoginActivity


class ConfirmDialogFragment : DialogFragment() {

    private lateinit var binding: FragmentConfirmDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentConfirmDialogBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog?.window?.requestFeature(Window.FEATURE_NO_TITLE)
        isCancelable = false

        goToMainActivity()

        return binding.root
    }

    private fun goToMainActivity() {
        val timer = object : CountDownTimer(2000, 1000) {
            override fun onTick(p0: Long) {
            }

            override fun onFinish() {
                val intent = Intent(requireContext(), MainActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                startActivity(intent)
                activity?.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                activity?.finish()
            }

        }
        timer.start()

    }


}