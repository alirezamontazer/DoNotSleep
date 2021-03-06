package com.alimonapps.donotsleep.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.setupWithNavController
import com.alimonapps.donotsleep.EyesTracker
import com.alimonapps.donotsleep.R
import com.alimonapps.donotsleep.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

/** Mohsen Shahbazi
 * This is the main activity which is the holder of home fragment
 */

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private val viewModel: MainViewModel by viewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        //setting up navigation controller for future updates
        navController = Navigation.findNavController(this, R.id.fragment)
//        binding.bottomNavigation.setupWithNavController(navController)


    }


}
