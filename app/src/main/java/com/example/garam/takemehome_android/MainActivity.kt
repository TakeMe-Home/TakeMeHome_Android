package com.example.garam.takemehome_android

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.garam.takemehome_android.network.NetworkController
import com.example.garam.takemehome_android.network.NetworkService
import com.example.garam.takemehome_android.restaurant.ForRestaurantActivity
import com.example.garam.takemehome_android.signUp.CustomerSignUpActivity
import com.example.garam.takemehome_android.signUp.RestaurantSignUpActivity
import com.example.garam.takemehome_android.signUp.RiderSignUpActivity
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_main.*
import org.json.JSONObject
import java.util.regex.Pattern
import retrofit2.*;

class MainActivity : AppCompatActivity() {

    private val networkService: NetworkService by lazy {
        NetworkController.instance.networkService
    }
    private lateinit var nextIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            requestPermissions(arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION),100)
        }
        val idInfo = IdText.text
        val pwInfo = passwordText.text

        val loginInfo = JSONObject()
        loginInfo.put("email",idInfo)
        loginInfo.put("password",pwInfo)

        val loginObj = JsonParser().parse(loginInfo.toString()) as JsonObject
        val items = arrayOf("Rider","Customer","Restaurant")
        val dialog = AlertDialog.Builder(this,android.R.style.Theme_DeviceDefault_Light_Dialog_Alert)

        loginText.setOnClickListener{
            when {
                idInfo.toString() == "" || !checkEmail(idInfo.toString()) -> {
                    Toast.makeText(this,"올바른 이메일 형식으로 입력하세요",Toast.LENGTH_LONG).show()
                }
                pwInfo.toString() == "" -> {
                    Toast.makeText(this,"비밀번호를 입력하세요",Toast.LENGTH_LONG).show()
                }
                else -> {
                    dialog.setTitle("로그인 유형을 선택하세요").setItems(items) {
                            dialogInterface, i -> login(i,loginObj)
                    }.setCancelable(false).show()
                }
            }
        }
        signUpText.setOnClickListener {
           dialog.setTitle("회원가입 유형을 선택하세요").setItems(items) { dialogInterface, i ->
               nextActivity(i)
           }.setCancelable(false).show()
        }
    }

    private fun login(i: Int, loginInfo: JsonObject ){
        when(i) {
            0 -> {
                networkService.signUpRider(loginInfo).enqueue(object : Callback<JsonObject>{
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        Toast.makeText(this@MainActivity, "로그인에 실패 하였습니다",
                            Toast.LENGTH_SHORT).show()
                    }

                    override fun onResponse(
                        call: Call<JsonObject>,
                        response: Response<JsonObject>
                    ) {
                        nextIntent = Intent(this@MainActivity, ForRiderActivity::class.java)
                        startActivity(nextIntent)
                    }
                })

            }
            1 -> {
                networkService.signUpCustomer(loginInfo).enqueue(object : Callback<JsonObject>{
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                    }

                    override fun onResponse(
                        call: Call<JsonObject>,
                        response: Response<JsonObject>
                    ) {

                    }
                })
            }
            2 -> {
                networkService.signUpRestaurant(loginInfo).enqueue(object : Callback<JsonObject>{
                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                    }

                    override fun onResponse(
                        call: Call<JsonObject>,
                        response: Response<JsonObject>
                    ) {

                    }
                })
                nextIntent = Intent(this@MainActivity, ForRestaurantActivity::class.java)
                startActivity(nextIntent)
            }
        }
    }

    private fun nextActivity(i: Int){
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
    private val EMAIL_ADDRESS_PATTERN : Pattern = Pattern.compile(
        "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                "\\@" +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                "(" +
                "\\." +
                "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                ")+"
    )
    private fun checkEmail(email: String): Boolean{
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches()
    }
}