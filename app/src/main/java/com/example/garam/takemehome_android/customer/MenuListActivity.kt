package com.example.garam.takemehome_android.customer

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.garam.takemehome_android.R
import com.example.garam.takemehome_android.network.NetworkController
import com.example.garam.takemehome_android.network.NetworkService
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.activity_menu_list.*
import kotlinx.android.synthetic.main.menu_dialog.*
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuListActivity : AppCompatActivity() {
    private lateinit var viewModel : MenuSharedViewModel
    private var menuLists = arrayListOf<MenuList>()
    private var choiceLists = arrayListOf<MenuChoiceList>()
    private val networkService: NetworkService by lazy {
        NetworkController.instance.networkService
    }

    private lateinit var dialog: Dialog
    private lateinit var menuRecycler:MenuListViewAdapter
    private lateinit var choiceRecycler: ChoiceListViewAdapter
    private var menuIdCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_list)
        val recycler = findViewById<RecyclerView>(R.id.menuRecycler)
        val choiceRecyclerView = findViewById<RecyclerView>(R.id.choiceRecyclerView)
        val intent = intent
        val restaurantId = intent.getIntExtra("restaurantId",0)
        val customerId = intent.getIntExtra("customerId",0)
        viewModel = ViewModelProvider(this).get(MenuSharedViewModel::class.java)
        menuLookUp(restaurantId)

        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.menu_dialog)

        menuRecycler = MenuListViewAdapter(menuLists,this){
            menuList ->
            showDialog(menuList)
        }

        recycler.adapter = menuRecycler
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.setHasFixedSize(true)

        choiceRecycler = ChoiceListViewAdapter(choiceLists,this)

        choiceRecyclerView.adapter = choiceRecycler
        choiceRecyclerView.layoutManager = LinearLayoutManager(this)
        choiceRecyclerView.setHasFixedSize(false)

        paymentButton.setOnClickListener {
            val receptionObject = JSONObject()
            val menuIdCounts = JSONObject()
            val menuIdArray = JSONArray()
            val menuArray = JSONObject()
            for (i in 0 .. menuIdCount) {
                menuArray.put("count", menuIdCount)
                menuArray.put("menuId", 0)
                menuIdArray.put(i,menuArray)
            }
            menuIdCounts.put("menuIdCounts",menuIdArray)

            receptionObject.put("customerId",customerId)
            receptionObject.put("menuIdCounts",menuIdCounts)
            receptionObject.put("paymentStatus","COMPLITE")
            receptionObject.put("paymentType","CARD")
            receptionObject.put("restaurantId",restaurantId)
            receptionObject.put("totalPrice",(paymentTextView.text as String).toInt())

            Log.e("reception", receptionObject.toString())
            val receptionInfo = JsonParser().parse(receptionObject.toString()).asJsonObject
            orderReception(receptionInfo)
        }

        payConfirmButton.setOnClickListener {
             paymentTextView.text = ""
        }
    }

    private fun orderReception(receptionInfo: JsonObject){
        networkService.reception(receptionInfo).enqueue(object : Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

            }
        })
    }

    private fun menuLookUp(id :Int){
        networkService.menuLookUp(id).enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val res = response.body()
                val message = res?.get("message")?.asString

                when{
                    message == "메뉴 조회 성공" ->{
                        val resObject = JSONObject(res.toString())
                        val data = resObject.getJSONArray("data")
                        for( i in 0 until data.length()){
                            val dataObject = data.getJSONObject(i)
                            menuLists.add(MenuList(dataObject.getString("name"),
                            dataObject.getInt("price"),
                                dataObject.getInt("id"),dataObject.getString("menuStatus")))
                        }
                        menuRecycler.notifyDataSetChanged()

                    }
                    else -> {

                    }
                }
            }
        })
    }

    private fun showDialog(menuList: MenuList){
        dialog.show()
        dialog.setCanceledOnTouchOutside(false)
        dialog.menuNameConfirm.text = menuList.menuName
        var menuCount = 0

        dialog.menuCountTextView.text = menuCount.toString()

        dialog.menuMinusButton.setOnClickListener {
            when{
                dialog.menuCountTextView.text.toString() == "0" ->{

                }
                else ->{
                    menuCount -= 1
                    dialog.menuCountTextView.text = menuCount.toString()
                }
            }
        }

        dialog.menuPlusButton.setOnClickListener {
            menuCount += 1
            dialog.menuCountTextView.text = menuCount.toString()
        }

        dialog.menuConfirmButton.setOnClickListener {
            /*menuDetailName.text = menuList.menuName
            menuDetailPrice.text = menuList.menuPrice.toString()
            menuToTalPrice.text = (((dialog.menuCountTextView.text as String).toInt()
                    * menuList.menuPrice.toInt()).toString())
            menuIdCount += 1
            */
            choiceLists.add(MenuChoiceList(menuList.menuName,menuList.menuPrice.toInt(),
                ((dialog.menuCountTextView.text as String).toInt()
                        * menuList.menuPrice.toInt())))
            choiceRecycler.notifyDataSetChanged()
            dialog.dismiss()
        }
    }
}