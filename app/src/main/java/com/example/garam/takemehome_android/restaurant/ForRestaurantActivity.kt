package com.example.garam.takemehome_android.restaurant

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.garam.takemehome_android.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class ForRestaurantActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_for_restaurant)
        val navView :BottomNavigationView = findViewById(R.id.nav_view_restaurant)
        val navController = findNavController(R.id.nav_host_fragment_restaurant)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_restaurant_home,R.id.navigation_restaurant_dashboard,
                R.id.navigation_restaurant_notifications,R.id.navigation_restaurant_update
            )
        )
        setupActionBarWithNavController(navController,appBarConfiguration)
        navView.setupWithNavController(navController)
    }
}