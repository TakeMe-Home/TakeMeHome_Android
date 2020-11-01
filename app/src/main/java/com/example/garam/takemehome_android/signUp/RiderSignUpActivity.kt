package com.example.garam.takemehome_android.signUp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.garam.takemehome_android.R
import com.example.garam.takemehome_android.network.NetworkController
import com.example.garam.takemehome_android.network.NetworkService
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_rider_sign_up.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class RiderSignUpActivity : AppCompatActivity() {

    private val networkService: NetworkService by lazy {
        NetworkController.instance.networkService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rider_sign_up)

        val signInfo = JSONObject()

        val email = riderEmail.text
        val name = riderName.text
        val password = riderPassword.text
        val phone = riderPhone.text

        riderSignUp.setOnClickListener {

            if (textCheck(email.toString(), name.toString(),password.toString(),phone.toString())) {
                signInfo.put("email", email)
                signInfo.put("name", name)
                signInfo.put("password", password)
                signInfo.put("phoneNumber", phone)
                val riderObject = JsonParser().parse(signInfo.toString()) as JsonObject
                Log.e("라이더 정보", "$riderObject")

                //  sign(riderObject)
            }
        }
    }

    private fun textCheck(email: String, name: String, password: String, phone: String): Boolean{

        when {
            email == "" -> {
                Toast.makeText(this,"이메일을 입력하세요",Toast.LENGTH_LONG).show()
                return false
            }
            !checkEmail(email) -> {
                Toast.makeText(this, "올바른 이메일 형식으로 입력하세요", Toast.LENGTH_LONG).show()
                return false
            }
            name == "" -> {
                Toast.makeText(this,"이름을 입력하세요",Toast.LENGTH_LONG).show()
                return false
            }
            password == "" -> {
                Toast.makeText(this,"비밀번호를 입력하세요",Toast.LENGTH_LONG).show()
                return false
            }
            phone == "" -> {
                Toast.makeText(this,"휴대폰 번호를 입력하세요",Toast.LENGTH_LONG).show()
                return false
            }
            !checkPhone(phone.toString()) -> {
                Toast.makeText(this, "올바른 휴대폰 번호 형식으로 입력하세요", Toast.LENGTH_LONG).show()
                return false
            }
            else ->{
                return true
            }
        }
    }

    private fun sign(riderInfo : JsonObject){
            val signUp = networkService.signUpRider(riderInfo)

           signUp.enqueue(object : Callback<JsonObject>{
                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                   Toast.makeText(this@RiderSignUpActivity,"회원가입에 실패하였습니다",Toast.LENGTH_LONG).show()
                }
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    Toast.makeText(this@RiderSignUpActivity,"회원가입에 성공하였습니다",Toast.LENGTH_LONG).show()
                    finish()
                }
            })
    }

    private val PHONE_NUMBER_PATTERN : Pattern = Pattern.compile(
        "01[016789][0-9]{3,4}[0-9]{4}$"
    )

    private fun checkPhone(phone: String): Boolean{
        return PHONE_NUMBER_PATTERN.matcher(phone).matches()
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