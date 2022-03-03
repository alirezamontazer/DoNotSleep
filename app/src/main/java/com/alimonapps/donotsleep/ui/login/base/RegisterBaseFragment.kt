package com.alimonapps.donotsleep.ui.login.base

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.alimonapps.donotsleep.R
import com.alimonapps.donotsleep.databinding.RegisterBaseFragmentBinding
import com.alimonapps.donotsleep.ui.login.step1.RegisterStep1Fragment
import com.alimonapps.donotsleep.ui.login.step2.RegisterStep2Fragment
import org.koin.androidx.viewmodel.ext.android.viewModel

private const val TAG = "RegisterBaseFragment"

class RegisterBaseFragment : Fragment() {

    private val viewModel: RegisterBaseViewModel by viewModel()
    private lateinit var binding: RegisterBaseFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = RegisterBaseFragmentBinding.inflate(inflater)
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setupViewPager()

        return binding.root
    }

    private fun setupViewPager() {
        binding.registerViewPager.setPagingEnabled(false)
        val adapter = CustomViewPagerAdapter(childFragmentManager)
        adapter.addFragment(RegisterStep1Fragment())
        adapter.addFragment(RegisterStep2Fragment())
        binding.registerViewPager.adapter = adapter
        binding.dotsIndicator.setViewPager(binding.registerViewPager)

        binding.registerViewPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                if (position == 0) {
                    binding.tvTitle2.text = "Step 1"
                } else {
                    binding.tvTitle2.text = "Step 2"
                }
            }

            override fun onPageSelected(position: Int) {
            }

            override fun onPageScrollStateChanged(state: Int) {
            }

        })

        viewModel.openViewPagerFragmentNumber.observe(viewLifecycleOwner) {
            if (it != -1) {
                Log.e(TAG, "openSecondViewPagerFragment observed")
                binding.registerViewPager.currentItem = it
            }
        }

        viewModel.openMainActivity.observe(viewLifecycleOwner) {
            if (it) {
                // TODO: go to main activity
            }

        }


    }


}