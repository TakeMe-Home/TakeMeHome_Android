package com.example.garam.takemehome_android.signUp

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.garam.takemehome_android.R
import com.example.garam.takemehome_android.network.KakaoApi
import com.example.garam.takemehome_android.network.NetworkController
import com.example.garam.takemehome_android.network.NetworkService
import com.example.garam.takemehome_android.network.NetworkServiceRestaurant
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

    private val networkService: NetworkServiceRestaurant by lazy {
        NetworkController.instance.networkServiceRestaurant
    }

    private var latitude : Double = 0.0
    private var longitude : Double= 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant_sign_up)

        val signInfo = JSONObject()
        val ownerSignUpRequest = JSONObject()
        val restaurantSaveRequest = JSONObject()
        val location = JSONObject()

        val email = restaurantEmail.text
        val name = restaurantName.text
        val password = restaurantPassword.text
        val phone = restaurantPhone.text
        val ownerPhone = ownerPhone.text
        val ownerName = ownerName.text
        val address = restaurantAddress.text
        var testAddress = ""

        detailAddress.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(
                s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(
                s: CharSequence, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable) {
                testAddress = s.toString()
            }
        })

        restaurantSignUp.setOnClickListener {
            if (textCheck(email.toString(), name.toString(), ownerName.toString(),
                    password.toString(), ownerPhone.toString(), latitude, longitude)) {

                location.put("x",latitude)
                location.put("y",longitude)

                ownerSignUpRequest.put("email", email)
                ownerSignUpRequest.put("name", ownerName)
                ownerSignUpRequest.put("password", password)
                ownerSignUpRequest.put("phoneNumber", ownerPhone)
                ownerSignUpRequest.put("address",testAddress)

                restaurantSaveRequest.put("address",testAddress)
                restaurantSaveRequest.put("location",location)
                restaurantSaveRequest.put("name",name)
                restaurantSaveRequest.put("number",phone)

                signInfo.put("ownerSignUpRequest",ownerSignUpRequest)
                signInfo.put("restaurantSaveWithoutIdRequest",restaurantSaveRequest)

                val restaurantObject = JsonParser().parse(signInfo.toString()) as JsonObject
                sign(restaurantObject)
            }
        }
        searchAddressButton.setOnClickListener {
             restaurantAddress(address.toString())
        }
    }

    private fun restaurantAddress(keyword: String) {
        val failMessage = Toast.makeText(this@RestaurantSignUpActivity,"주소 검색에 실패하였습니다"
            ,Toast.LENGTH_LONG)
        val retrofit: Retrofit = Retrofit.Builder().baseUrl(KakaoApi.instance.KakaoURL)
            .addConverterFactory(GsonConverterFactory.create()).build()

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
                val meta = res?.getAsJsonObject("meta")
                val totalCount = meta?.get("total_count")?.asInt
                if (totalCount == 1) {
                    val add = documents?.asJsonArray?.get(0)
                    val addInfo = add?.asJsonObject?.get("address")
                    when {
                        addInfo != null -> {
                            val addressName = JSONObject(addInfo.toString()).getString("address_name")
                            val x = JSONObject(addInfo.toString()).getDouble("x")
                            val y = JSONObject(addInfo.toString()).getDouble("y")
                            latitude = y
                            longitude = x
                            detailAddress.setText(addressName)
                            Toast.makeText(this@RestaurantSignUpActivity, "주소 검색에 성공하였습니다",
                                Toast.LENGTH_LONG).show()
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

    private fun sign(restaurantInfo : JsonObject){
        val signUp = networkService.signUpRestaurant(restaurantInfo)
        val failMessage = Toast.makeText(this@RestaurantSignUpActivity,"회원가입에 실패하였습니다"
            , Toast.LENGTH_LONG)

        signUp.enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                failMessage.show()
            }
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val res = response.body()?.asJsonObject
                val message = res?.get("message")?.asString
                when  {
                    message.equals("가게 주인 등록 성공") -> {
                        Toast.makeText(this@RestaurantSignUpActivity,"회원가입에 성공하였습니다"
                            , Toast.LENGTH_LONG).show()
                        finish()
                    }
                    message.equals("가게 주인 등록 실패") || response.body().toString() == "null" -> {
                        failMessage.show()
                    }
                }
            }
        })
    }
    private fun textCheck(email: String, name: String, ownerName: String,password: String
                          , phone: String, x: Double, y: Double): Boolean{

        when {
            email == "" || !checkEmail(email) -> {
                Toast.makeText(this,"올바른 이메일 형식으로 입력하세요", Toast.LENGTH_LONG).show()
                return false
            }
            name == "" -> {
                Toast.makeText(this,"가게 이름을 입력하세요", Toast.LENGTH_LONG).show()
                return false
            }
            ownerName == "" ->{
                Toast.makeText(this,"이름을 입력하세요", Toast.LENGTH_LONG).show()
                return false
            }
            password == "" -> {
                Toast.makeText(this,"비밀번호를 입력하세요", Toast.LENGTH_LONG).show()
                return false
            }
            phone == "" || !checkPhone(phone) -> {
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