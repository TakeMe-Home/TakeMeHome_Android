package com.example.garam.takemehome_android.signUp

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.garam.takemehome_android.R
import com.example.garam.takemehome_android.network.NetworkController
import com.example.garam.takemehome_android.network.NetworkService
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_customer_sign_up.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.regex.Pattern

class CustomerSignUpActivity : AppCompatActivity() {

    private val networkService: NetworkService by lazy {
        NetworkController.instance.networkService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_sign_up)

        val signInfo = JSONObject()

        val email = customerEmail.text
        val name = customerName.text
        val password = customerPassword.text
        val phone = customerPhone.text

        customerSignUp.setOnClickListener {

            if (textCheck(email.toString(), name.toString(),password.toString(),phone.toString())) {
                signInfo.put("email", email)
                signInfo.put("name", name)
                signInfo.put("password", password)
                signInfo.put("phoneNumber", phone)
                val customerObject = JsonParser().parse(signInfo.toString()) as JsonObject
                Log.e("고객 정보", "$customerObject")

                //  sign(customerObject)
            }
        }
    }

    private fun sign(customerInfo : JsonObject){
        val signUp = networkService.signUpCustomer(customerInfo)

        signUp.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(this@CustomerSignUpActivity,"회원가입에 실패하였습니다",Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Toast.makeText(this@CustomerSignUpActivity,"회원가입에 성공하였습니다",Toast.LENGTH_LONG).show()
                finish()
            }
        })
    }

    private fun textCheck(email: String, name: String, password: String, phone: String): Boolean{

        when {
            email == "" -> {
                Toast.makeText(this,"이메일을 입력하세요", Toast.LENGTH_LONG).show()
                return false
            }
            !checkEmail(email) -> {
                Toast.makeText(this, "올바른 이메일 형식으로 입력하세요", Toast.LENGTH_LONG).show()
                return false
            }
            name == "" -> {
                Toast.makeText(this,"이름을 입력하세요", Toast.LENGTH_LONG).show()
                return false
            }
            password == "" -> {
                Toast.makeText(this,"비밀번호를 입력하세요", Toast.LENGTH_LONG).show()
                return false
            }
            phone == "" -> {
                Toast.makeText(this,"휴대폰 번호를 입력하세요", Toast.LENGTH_LONG).show()
                return false
            }
            !checkPhone(phone) -> {
                Toast.makeText(this, "올바른 휴대폰 번호 형식으로 입력하세요", Toast.LENGTH_LONG).show()
                return false
            }
            else ->{
                return true
            }
        }
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