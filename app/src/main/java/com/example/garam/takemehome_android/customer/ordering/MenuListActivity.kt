package com.example.garam.takemehome_android.customer.ordering

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Window
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.garam.takemehome_android.R
import com.example.garam.takemehome_android.customer.*
import com.example.garam.takemehome_android.network.NetworkController
import com.example.garam.takemehome_android.network.NetworkService
import com.google.gson.JsonObject
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
    private var totalPayPrice = 0
    private var menuCountSize = 0

    private lateinit var dialog: Dialog
    private lateinit var menuRecycler: MenuListViewAdapter
    private lateinit var choiceRecycler: ChoiceListViewAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_list)
        val recycler = findViewById<RecyclerView>(R.id.menuRecycler)
        val choiceRecyclerView = findViewById<RecyclerView>(R.id.choiceRecyclerView)
        val intent = intent
        val restaurantId = intent.getIntExtra("restaurantId",0)
        var customerId = intent.getIntExtra("customerId",0)

        val restaurantName = intent.getStringExtra("restaurantName")
        viewModel = ViewModelProvider(this).get(MenuSharedViewModel::class.java)

        viewModel.setCustomerId(customerId)
        customerId = viewModel.getCustomerId()
        menuLookUp(restaurantId)

        dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.menu_dialog)

        menuRecycler =
            MenuListViewAdapter(
                menuLists,
                this
            ) { menuList ->
                when (menuList.menuStatus) {
                    "SOLD_OUT" -> {
                        recycler.isEnabled = false
                    }
                    else -> {
                        showDialog(menuList)
                    }
                }

            }

        recycler.adapter = menuRecycler
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.setHasFixedSize(true)

        choiceRecycler =
            ChoiceListViewAdapter(
                choiceLists,
                this
            )

        choiceRecyclerView.adapter = choiceRecycler
        choiceRecyclerView.layoutManager = LinearLayoutManager(this)
        choiceRecyclerView.setHasFixedSize(false)

        paymentButton.setOnClickListener {
            menuCountSize = viewModel.getCountInfo()

            val menuIdCounts = JSONObject()
            val menuIdArray = JSONArray()

            val orderInfo = JSONObject()
            val menuNameCounts = JSONObject()
            val menuNameArray = JSONArray()

            for (i in 0 until menuCountSize) {
                menuIdArray.put(i,viewModel.getMenuArray()[i])
                menuNameArray.put(i,viewModel.getMenuNameArray()[i])
            }
            menuIdCounts.put("menuIdCounts",menuIdArray)

            menuNameCounts.put("menuNameCounts",menuNameArray)

            when {
                menuCountSize == 0 || (paymentTextView.text as String).toString() == "0원" -> {
                    Toast.makeText(this,"메뉴를 선택해주세요",Toast.LENGTH_LONG).show()
                }

                 (paymentTextView.text as String).toString() == "" ||
                         (paymentTextView.text as String).toString() == "0원" ->{
                    Toast.makeText(this,"결제 금액 조회를 눌러주세요",Toast.LENGTH_LONG).show()
                }

                else -> {

                    orderInfo.put("customerId",customerId)
                    orderInfo.put("menuNameCounts",menuNameCounts)
                    orderInfo.put("restaurantId",restaurantId)
                    orderInfo.put("totalPrice",viewModel.getLastPayPrice())

                    val nextIntent = Intent(this, PaymentActivity::class.java)
                    nextIntent.putExtra("lastPrice",viewModel.getLastPayPrice())
                    nextIntent.putExtra("restaurantName",restaurantName)
                    nextIntent.putExtra("restaurantId",restaurantId)
                    nextIntent.putExtra("customerId",customerId)

                    nextIntent.putExtra("orderInfo",orderInfo.toString())
                    startActivity(nextIntent)
                }
            }
        }

        payConfirmButton.setOnClickListener {
            menuCountSize = viewModel.getCountInfo()
            when (menuCountSize) {
                0 -> {
                    Toast.makeText(this,"메뉴를 선택해주세요",Toast.LENGTH_LONG).show()
                }
                else -> {

                }
            }
            paymentTextView.text = viewModel.getLastPayPrice().toString() + "원"
        }
    }

    private fun menuLookUp(id :Int){
        val failMessage = Toast.makeText(this@MenuListActivity,"메뉴 조회에 실패하였습니다"
            ,Toast.LENGTH_LONG)
        networkService.menuLookUp(id).enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                failMessage.show()
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val res = response.body()

                when (res?.get("message")?.asString) {
                    "메뉴 조회 성공" -> {
                        val resObject = JSONObject(res.toString())
                        val data = resObject.getJSONArray("data")
                        for( i in 0 until data.length()){
                            val dataObject = data.getJSONObject(i)
                            menuLists.add(
                                MenuList(
                                    dataObject.getString("name"),
                                    dataObject.getInt("price"),
                                    dataObject.getInt("id"), dataObject.getString("menuStatus")
                                )
                            )
                        }
                        menuRecycler.notifyDataSetChanged()

                    }
                    "메뉴 조회 실패" -> {
                        failMessage.show()
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

            when{
                (dialog.menuCountTextView.text as String).toInt() != 0 -> {
                    choiceLists.add(
                        MenuChoiceList(
                            menuList.menuName, menuList.menuPrice.toInt(),
                            ((dialog.menuCountTextView.text as String).toInt()
                                    * menuList.menuPrice.toInt())
                        )
                    )
                    val menuArray = JSONObject()
                    val menuNameArray = JSONObject()

                    menuArray.put("count" , (dialog.menuCountTextView.text as String).toInt())
                    menuArray.put("menuId",menuList.menuId)

                    menuNameArray.put("count",(dialog.menuCountTextView.text as String).toInt())
                    menuNameArray.put("name",dialog.menuNameConfirm.text as String)

                    viewModel.setMenuArray(menuArray)
                    viewModel.setMenuNameArray(menuNameArray)

                    viewModel.setCountInfo(menuList.menuId,(dialog.menuCountTextView.text as String).toInt())
                    viewModel.setLastPayPrice((dialog.menuCountTextView.text as String).toInt()
                            * menuList.menuPrice.toInt())
                    choiceRecycler.notifyDataSetChanged()
                    dialog.dismiss()
                }
                (dialog.menuCountTextView.text as String).toInt() == 0 -> {
                    dialog.dismiss()
                }
            }

        }
    }
}