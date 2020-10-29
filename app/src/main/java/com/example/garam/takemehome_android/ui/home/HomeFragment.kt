package com.example.garam.takemehome_android.ui.home

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.garam.takemehome_android.R
import com.example.garam.takemehome_android.network.KakaoApi
import com.example.garam.takemehome_android.network.NetworkController
import com.example.garam.takemehome_android.network.NetworkService
import com.example.garam.takemehome_android.ui.SharedViewModel
import com.example.garam.takemehome_android.ui.dashboard.location_List
import com.google.gson.JsonObject
import kotlinx.android.synthetic.main.confirm_dialog.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class HomeFragment : Fragment() {
    private var lists = arrayListOf<call_List>()
    private lateinit var dialog : Dialog
    private lateinit var callRecycler : CallViewAdapter
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private var loc_lists = arrayListOf<location_List>()

    /*val networkService : NetworkService by lazy {
        NetworkController.instance.networkService
    } */
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProviders.of(this).get(HomeViewModel::class.java)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_home, container, false)
        val recycler = root.findViewById<RecyclerView>(R.id.callRecycler)

        lists.add(call_List("곱창고","굴포로81","오후8시"))
        lists.add(call_List("라무진","충선로209번길 13","오후9시"))
        lists.add(call_List("드롭탑","갈산2동","오후6시"))
        lists.add(call_List("스타벅스","갈산1동","오후2시"))

        dialog = Dialog(root.context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.confirm_dialog)

        callRecycler = CallViewAdapter(lists, root.context) { callList ->
            Log.e("홈 프래그먼트","콜 리스트 눌러짐")
            showDialog(callList)
        }

        recycler.adapter = callRecycler
      //  callRecycler.notifyDataSetChanged()
        recycler.layoutManager = LinearLayoutManager(root.context)
        recycler.setHasFixedSize(true)

        return root
    }

    private fun showDialog(callList: call_List){
        dialog.show()
        dialog.setCanceledOnTouchOutside(false)
        dialog.testNameConfirm.text = callList.storeName

        dialog.testConfirmbutton.setOnClickListener {
            sharedViewModel.setData(callList)
            searchLocation(callList)
            dialog.dismiss()
            lists.remove(callList)
            callRecycler.notifyDataSetChanged()
        }

        dialog.testCancelbutton.setOnClickListener {
            dialog.dismiss()
        }
    }

    private fun searchLocation(callList: call_List){

        val retrofit: Retrofit = Retrofit.Builder().baseUrl(KakaoApi.instance.KakaoURL).addConverterFactory(
            GsonConverterFactory.create()).build()

        val networkService = retrofit.create(NetworkService::class.java)
        val testAddress : Call<JsonObject> = networkService.address(
            KakaoApi.instance.kakaoKey,
            callList.storeAddress
        )
        Log.e("카카오 키 ", KakaoApi.instance.kakaoKey)
        testAddress.enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val res = response.body()
                val body = response.code()
                val fa = response.message()
                Log.e("바디", "$body")
                Log.e("메시지", fa)
                Log.e("리스폰스", res.toString())
                if (response.isSuccessful) {
                    val kakao = res?.getAsJsonArray("documents")
                    Log.e("카카오","$kakao")
                    val add = kakao?.asJsonArray?.get(0)
                    Log.e("ㄹㅁ","$add")
                    val addInfo = add?.asJsonObject?.get("address")
                    val x = JSONObject(addInfo.toString()).getString("x")
                    val y = JSONObject(addInfo.toString()).getString("y")
                    loc_lists.add(location_List(x,y))
                    sharedViewModel.setLocation(loc_lists[loc_lists.lastIndex])
                    Log.e("검색한 주소 좌표:" , "$x + $y")
                }
            }
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                //Toast.makeText(.context,"실패",Toast.LENGTH_LONG)
            }
        })
    }

}