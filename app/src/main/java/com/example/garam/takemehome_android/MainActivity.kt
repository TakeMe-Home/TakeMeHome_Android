package com.example.garam.takemehome_android

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.garam.takemehome_android.signUp.CustomerSignUpActivity
import com.example.garam.takemehome_android.signUp.RestaurantSignUpActivity
import com.example.garam.takemehome_android.signUp.RiderSignUpActivity
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION),100)
        }

        val but = findViewById<TextView>(R.id.loginText)
        but.setOnClickListener{
            val nextIntent = Intent(this,ForRiderActivity::class.java)
            startActivity(nextIntent)

        }

        val items = arrayOf("Rider","Customer","Restaurant")

        val dialog = AlertDialog.Builder(this,android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)

       signUpText.setOnClickListener {
           dialog.setTitle("회원가입 유형을 선택하세요").setItems(items,DialogInterface.OnClickListener {
                   dialogInterface, i ->
                   nextActivity(i)

           }).setCancelable(false).show()


        }
    }

    private fun nextActivity(i: Int){
        val nextIntent: Intent
        when (i) {
            0 -> {
                nextIntent = Intent(this, RiderSignUpActivity::class.java)
                startActivity(nextIntent)
            }
            1 -> {
                nextIntent = Intent(this, CustomerSignUpActivity::class.java)
                startActivity(nextIntent)
            }
            2 -> {
                nextIntent = Intent(this, RestaurantSignUpActivity::class.java)
                startActivity(nextIntent)
            }
        }
    }

}