package com.example.final_nextstop.ui

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.final_nextstop.R
import com.example.final_nextstop.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {


    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // אתחול ViewBinding
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // מציאת ה־NavHostFragment וה־NavController
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController


        binding.profileButton.setOnClickListener {
            navController.navigate(R.id.profileFragment)
        }

        binding.homeButton.setOnClickListener {
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.my_nav, true)
                .setLaunchSingleTop(true)
                .build()
            navController.navigate(R.id.homeFragment, null, navOptions)
        }

    }
}