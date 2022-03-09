package com.alimonapps.donotsleep.ui.setting

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alimonapps.donotsleep.R
import com.alimonapps.donotsleep.databinding.HomeFragmentBinding
import com.alimonapps.donotsleep.databinding.SettingFragmentBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

// This class is empty for now and it's not in the app. This is for future updates

class SettingFragment : Fragment() {

    private val viewModel: SettingViewModel by viewModel()
    private lateinit var binding: SettingFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = SettingFragmentBinding.inflate(inflater)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        binding.text.text = "Miladi"

        return binding.root


    }


}