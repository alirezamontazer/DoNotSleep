package com.alimonapps.donotsleep.ui.login.step2

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.alimonapps.donotsleep.databinding.RegisterStep2FragmentBinding
import com.alimonapps.donotsleep.ui.login.base.RegisterBaseViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterStep2Fragment : Fragment() {

    private val viewModel: RegisterStep2ViewModel by viewModel()
    private val sharedViewModel: RegisterBaseViewModel by sharedViewModel()
    private lateinit var binding: RegisterStep2FragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RegisterStep2FragmentBinding.inflate(inflater)



        return binding.root
    }


}