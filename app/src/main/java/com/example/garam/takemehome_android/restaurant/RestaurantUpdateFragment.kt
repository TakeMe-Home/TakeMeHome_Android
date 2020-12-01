package com.example.garam.takemehome_android.restaurant

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Toast
import androidx.core.graphics.rotationMatrix
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.garam.takemehome_android.R
import com.example.garam.takemehome_android.network.NetworkController
import com.example.garam.takemehome_android.network.NetworkServiceRestaurant
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.android.synthetic.main.fragment_restaurant_update.view.*
import kotlinx.android.synthetic.main.menu_register_dialog_layout.*
import kotlinx.android.synthetic.main.menu_update_dialog_layout.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RestaurantUpdateFragment : Fragment() {
    private val networkService: NetworkServiceRestaurant by lazy {
        NetworkController.instance.networkServiceRestaurant
    }

    private var menuStatusList = arrayListOf<MenuStatusList>()
    private lateinit var menuStatusRecycler : MenuStatusViewAdapter
    private lateinit var dialog : Dialog
    private lateinit var updateDialog : Dialog
    private lateinit var sharedViewModel: RestaurantSharedViewModel
    private lateinit var root : View

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        root = inflater.inflate(R.layout.fragment_restaurant_update, container, false)

        val recycler = root.findViewById<RecyclerView>(R.id.menuStatusRecyclerView)
        sharedViewModel = ViewModelProvider(requireActivity()).get(RestaurantSharedViewModel::class.java)
        val restaurantId = sharedViewModel.getId()

        menuLookUp(restaurantId!!)

        dialog = Dialog(root.context)
        dialog.setContentView(R.layout.menu_register_dialog_layout)

        updateDialog = Dialog(root.context)
        updateDialog.setContentView(R.layout.menu_update_dialog_layout)

        menuStatusRecycler = MenuStatusViewAdapter(menuStatusList,root.context){
            menuStatusList ->
            menuUpdateDialog(menuStatusList)

        }

        root.menuAddButton.setOnClickListener {
            menuAddDialog()
        }

        recycler.adapter = menuStatusRecycler
        recycler.layoutManager = LinearLayoutManager(root.context)
        recycler.setHasFixedSize(true)

        return root
    }


    private fun menuLookUp(id: Int){
        val failMessage = Toast.makeText(root.context,"메뉴 조회에 실패하였습니다"
            ,Toast.LENGTH_LONG)

        networkService.menuLookUp(id).enqueue(object : Callback<JsonObject>{
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
                            menuStatusList.add(MenuStatusList(dataObject.getString("name"),
                                dataObject.getInt("price"),
                                dataObject.getString("menuStatus"),
                            dataObject.getInt("id")))
                        }
                        menuStatusRecycler.notifyDataSetChanged()

                    }
                    message == "메뉴 조회 실패" -> {
                        failMessage.show()
                    }
                }
            }
        })
    }


    private fun menuRegister(menuInfo : JsonObject){
        val failMessage = Toast.makeText(root.context,"메뉴 등록에 실패하였습니다", Toast.LENGTH_LONG)
        networkService.menuRequest(menuInfo).enqueue(object : Callback<JsonObject> {
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                failMessage.show()
            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                val res = response.body()
                when(res?.get("message")?.asString) {
                    "메뉴 등록 성공" -> {
                        //menuStatusList.add(MenuStatusList(menuInfo.get("name").asString,
                        //menuInfo.get("price").asInt,"SALE"))
                        dialog.dismiss()
                        //menuStatusRecycler.notifyDataSetChanged()
                        Toast.makeText(root.context,"메뉴를 성공적으로 등록했습니다",Toast.LENGTH_LONG).show()
                    }
                    "메뉴 등록 실패" -> {
                        failMessage.show()
                    }

                }
            }
        })
    }

    private fun menuUpdate(id: Int, updateInfo : JsonObject){
        networkService.menuUpdate(id,updateInfo).enqueue(object : Callback<JsonObject>{
            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

            }

            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                updateDialog.dismiss()

            }
        })
    }

    private fun menuUpdateDialog(menuUpdateInfo: MenuStatusList){
        updateDialog.show()
        updateDialog.setCanceledOnTouchOutside(false)
        val menuUpdateJson = JSONObject()
        updateDialog.menuUpdateName.setText(menuUpdateInfo.menuName)
        updateDialog.menuUpdatePrice.setText(menuUpdateInfo.menuPrice.toString())

        var menuStatus = ""

        updateDialog.menuUpdateStatus.setOnCheckedChangeListener { radioGroup, i ->
            when(i){
                R.id.menuUpdateSell -> {
                }
                R.id.menuUpdateSoldOUt -> {
                }
            }
        }

        updateDialog.menuUpdateConfirmButton.setOnClickListener {
            val name = updateDialog.menuUpdateName.text
            val price = updateDialog.menuUpdatePrice.text

            menuUpdateJson.put("name",name.toString())
            menuUpdateJson.put("price",price.toString().toInt())
            menuUpdateJson.put("menuStatus","SALE")

            val menuUpdateRequestInfo = JsonParser().parse(menuUpdateJson.toString()).asJsonObject

            menuUpdate(menuUpdateInfo.menuId,menuUpdateRequestInfo)
        }

        updateDialog.menuUpdateCancelButton.setOnClickListener {
            updateDialog.dismiss()
        }
    }

    private fun menuAddDialog(){
        val menu = JSONObject()
        dialog.show()
        dialog.setCanceledOnTouchOutside(false)

        dialog.menuRegisterConfirmButton.setOnClickListener {
            val name = dialog.menuRegisterName.text
            val price = dialog.menuRegisterPrice.text

            menu.put("name",name.toString())
            menu.put("price",price.toString().toInt())
            menu.put("restaurantId",sharedViewModel.getId())

            val json = JsonParser().parse(menu.toString()).asJsonObject
            menuRegister(json)
        }
    }


}