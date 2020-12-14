package com.example.garam.takemehome_android.restaurant

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.garam.takemehome_android.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class ForRestaurantActivity : AppCompatActivity() {
    private var backKeyPressedTime: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_for_restaurant)
        val sharedViewModel = ViewModelProvider(this).get(RestaurantSharedViewModel::class.java)
        val navView : BottomNavigationView = findViewById(R.id.nav_view_restaurant)
        val intent = intent
        val restaurantId = intent.getIntExtra("restaurantId",0)
        val restaurantAddress = intent.getStringExtra("restaurantAddress")
        val restaurantName = intent.getStringExtra("restaurantName")
        sharedViewModel.setId(restaurantId)

        val bundle = bundleOf("id" to restaurantId, "address" to restaurantAddress
            , "restaurantName" to restaurantName)

        val navController = findNavController(R.id.nav_host_fragment_restaurant)
        navController.navigate(R.id.navigation_restaurant_home,bundle)

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_restaurant_home,R.id.navigation_restaurant_dashboard,
                R.id.navigation_restaurant_notifications,R.id.navigation_restaurant_update
            )
        )

        setupActionBarWithNavController(navController,appBarConfiguration)
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