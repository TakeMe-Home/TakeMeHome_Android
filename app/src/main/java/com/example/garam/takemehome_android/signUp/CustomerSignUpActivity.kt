package com.example.garam.takemehome_android.signUp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.garam.takemehome_android.R
import com.example.garam.takemehome_android.network.KakaoApi
import com.example.garam.takemehome_android.network.NetworkController
import com.example.garam.takemehome_android.network.NetworkService
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_customer_sign_up.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.regex.Pattern

class CustomerSignUpActivity : AppCompatActivity() {

    private val networkService: NetworkService by lazy {
        NetworkController.instance.networkService
    }
    private var latitude : Double = 0.0
    private var longitude : Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_sign_up)

        val signInfo = JSONObject()
        val location = JSONObject()
        val email = customerEmail.text
        val name = customerName.text
        val password = customerPassword.text
        val phone = customerPhone.text
        val address = customerAddress.text
        var detailAddress = ""

        customerSignUp.setOnClickListener {

            if (textCheck(email.toString(), name.toString(),password.toString(),
                    phone.toString(), latitude, longitude)) {

                location.put("x",latitude)
                location.put("y",longitude)

                signInfo.put("email", email)
                signInfo.put("name", name)
                signInfo.put("password", password)
                signInfo.put("phoneNumber", phone)
                signInfo.put("address",detailAddress)
                signInfo.put("location",location)

                val customerObject = JsonParser().parse(signInfo.toString()) as JsonObject

                sign(customerObject)
            }
        }

        customerDetailAddress.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
                detailAddress = p0.toString()
            }
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
        })
        searchDetailAddressButton.setOnClickListener {
            customerAddress(address.toString())
        }
    }

    private fun customerAddress(keyword: String) {
        val retrofit: Retrofit = Retrofit.Builder().baseUrl(KakaoApi.instance.KakaoURL).addConverterFactory(
            GsonConverterFactory.create()).build()
        val failMessage = Toast.makeText(this@CustomerSignUpActivity,"주소 검색에 실패하였습니다",Toast.LENGTH_LONG)
        val networkService = retrofit.create(NetworkService::class.java)
        val addressSearch : Call<JsonObject> = networkService.address(
            KakaoApi.instance.kakaoKey,
            keyword
        )
        addressSearch.enqueue(object : Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                failMessage.show()
            }
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val res = response.body()

                val documents = res?.getAsJsonArray("documents")
                val test = res?.getAsJsonObject("meta")
                val totalCount = test?.get("total_count")?.asInt
                if (totalCount == 1) {
                    Log.e("카카오", "$documents")
                    val add = documents?.asJsonArray?.get(0)
                    Log.e("ㄹㅁ", "$add")
                    val addInfo = add?.asJsonObject?.get("address")
                    when {
                        addInfo != null -> {
                            val addressName =
                                JSONObject(addInfo.toString()).getString("address_name")
                            val x = JSONObject(addInfo.toString()).getDouble("x")
                            val y = JSONObject(addInfo.toString()).getDouble("y")
                            latitude = y
                            longitude = x

                            customerDetailAddress.setText(addressName)
                            Toast.makeText(
                                this@CustomerSignUpActivity,
                                "주소 검색에 성공하였습니다",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                        else -> {
                            failMessage.show()
                        }
                    }
                } else {
                    failMessage.show()
                }
            }
        })
    }

    private fun sign(customerInfo : JsonObject){
        val signUp = networkService.signUpCustomer(customerInfo)
        val failMessage = Toast.makeText(this@CustomerSignUpActivity,"회원가입에 실패하였습니다"
            ,Toast.LENGTH_LONG)
        signUp.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                failMessage.show()
            }
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val res = response.body()?.asJsonObject
                val message = res?.get("message")?.asString
                when {
                    message.equals("고객 회원 가입 성공") -> {
                        Toast.makeText(this@CustomerSignUpActivity,
                            "회원가입에 성공하였습니다", Toast.LENGTH_LONG).show()
                            finish ()
                    }
                    message.equals("고객 회원 가입 실패") || response.body().toString().equals("null")-> {
                        failMessage.show()
                    }
                }
            }
        })
    }

    private fun textCheck(email: String, name: String, password: String,
                          phone: String, x: Double, y: Double): Boolean{

        when {
            email == "" || !checkEmail(email)-> {
                Toast.makeText(this,"올바른 이메일 형식으로 입력하세요", Toast.LENGTH_LONG).show()
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
            phone == "" || !checkPhone(phone)-> {
                Toast.makeText(this,"올바른 휴대폰 번호 형식으로 입력하세요", Toast.LENGTH_LONG).show()
                return false
            }
            x == 0.0 || y == 0.0 -> {
                Toast.makeText(this,"주소를 검색하세요", Toast.LENGTH_LONG).show()
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