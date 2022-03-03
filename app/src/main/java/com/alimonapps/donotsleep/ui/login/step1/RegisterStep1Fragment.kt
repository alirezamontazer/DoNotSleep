package com.alimonapps.donotsleep.ui.login.step1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alimonapps.donotsleep.databinding.RegisterStep1FragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterStep1Fragment : Fragment() {

    private val viewModel: RegisterStep1ViewModel by viewModel()
    private lateinit var binding: RegisterStep1FragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RegisterStep1FragmentBinding.inflate(inflater)



        return binding.root
    }


}