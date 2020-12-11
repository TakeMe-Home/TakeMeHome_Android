package com.example.garam.takemehome_android

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.garam.takemehome_android.ui.SharedViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class ForRiderActivity : AppCompatActivity() {
    private var backKeyPressedTime: Long = 0
    private lateinit var navView: BottomNavigationView
    private lateinit var sharedViewModel: SharedViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_for_rider)
        navView = findViewById(R.id.nav_view)
        val intent = intent
        val riderId = intent.getIntExtra("riderId",0)

        sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
        sharedViewModel.setRiderId(riderId)

        val bundle = bundleOf("id" to riderId)

        val navController = findNavController(R.id.nav_host_fragment)
        navController.navigate(R.id.navigation_home,bundle)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard,
                R.id.navigation_notifications, R.id.navigation_update
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        navView.setOnNavigationItemReselectedListener(ItemSelectedListener())
    }

    class ItemSelectedListener : BottomNavigationView.OnNavigationItemReselectedListener{
        override fun onNavigationItemReselected(item: MenuItem) {
        }
    }

    override fun onBackPressed() {
        lateinit var toast: Toast
        when {
            System.currentTimeMillis() >= backKeyPressedTime + 1500 -> {
                backKeyPressedTime = System.currentTimeMillis()
                toast = Toast.makeText(this, "종료 하려면 한번 더 누르세요.", Toast.LENGTH_LONG)
                toast.show()
                return
            }
            System.currentTimeMillis() < backKeyPressedTime + 1500 -> {
                finish()
            }
        }
    }
}