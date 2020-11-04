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
import kotlinx.android.synthetic.main.activity_restaurant_sign_up.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.regex.Pattern

class RestaurantSignUpActivity : AppCompatActivity() {

    private val networkService: NetworkService by lazy {
        NetworkController.instance.networkService
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_sign_up)

        val signInfo = JSONObject()

        val email = restaurantEmail.text
        val name = restaurantName.text
        val password = restaurantPassword.text
        val phone = restaurantPhone.text
        val address = restaurantAddress.text
        var testAddress = ""

        detailAddress.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence,
                start: Int,
                count: Int,
                after: Int
            ) {
            }

            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) {
            }

            override fun afterTextChanged(s: Editable) {
                testAddress = s.toString()
            }
        })

        restaurantSignUp.setOnClickListener {
            if (textCheck(email.toString(), name.toString(),password.toString(),phone.toString())) {
                signInfo.put("email", email)
                signInfo.put("name", name)
                signInfo.put("password", password)
                signInfo.put("number", phone)
                signInfo.put("",testAddress)
                val restaurantObject = JsonParser().parse(signInfo.toString()) as JsonObject
                Log.e("Restaurant 정보", "$restaurantObject")

                sign(restaurantObject)
            }
        }
        searchAddressButton.setOnClickListener {
             restaurantAddress(address.toString())
        }
    }

    private fun restaurantAddress(keyword: String) {
        val retrofit: Retrofit = Retrofit.Builder().baseUrl(KakaoApi.instance.KakaoURL).addConverterFactory(
            GsonConverterFactory.create()).build()

        val networkService = retrofit.create(NetworkService::class.java)
        val addressSearch : Call<JsonObject> = networkService.address(
            KakaoApi.instance.kakaoKey,
            keyword
        )
        addressSearch.enqueue(object : Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
               Toast.makeText(this@RestaurantSignUpActivity,"주소 검색에 실패하였습니다",Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val res = response.body()
                val body = response.code()
                val fa = response.message()
                Log.e("바디", "$body")
                Log.e("메시지", fa)
                Log.e("리스폰스", res.toString())
                val kakao = res?.getAsJsonArray("documents")
                val test = res?.getAsJsonObject("meta")
                val total_count = test?.get("total_count")?.asInt
                if (total_count == 1) {
                    Log.e("카카오", "$kakao")
                    val add = kakao?.asJsonArray?.get(0)
                    Log.e("ㄹㅁ", "$add")
                    val addInfo = add?.asJsonObject?.get("address")
                    if (addInfo != null) {
                        val address_name = JSONObject(addInfo.toString()).getString("address_name")
                        val x = JSONObject(addInfo.toString()).getString("x")
                        val y = JSONObject(addInfo.toString()).getString("y")
                        Log.e("검색한 주소 좌표:", "$x + $y")
                        detailAddress.setText(address_name)
                        Log.e("도로명 주소 : ", address_name)
                        Toast.makeText(
                            this@RestaurantSignUpActivity,
                            "주소 검색에 성공하였습니다",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Log.e("null", "null")
                    }
                } else {
                    Toast.makeText(this@RestaurantSignUpActivity,"주소 검색에 실패하였습니다",Toast.LENGTH_LONG).show()
                }
            }
        })
    }

    private fun sign(restaurantInfo : JsonObject){
        val signUp = networkService.signUpRestaurant(restaurantInfo)

        signUp.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                Toast.makeText(this@RestaurantSignUpActivity,"회원가입에 실패하였습니다", Toast.LENGTH_LONG).show()
            }
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                Toast.makeText(this@RestaurantSignUpActivity,"회원가입에 성공하였습니다", Toast.LENGTH_LONG).show()
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