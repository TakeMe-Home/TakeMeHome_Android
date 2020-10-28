package com.example.garam.takemehome_android.ui.notifications

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.garam.takemehome_android.R
import com.example.garam.takemehome_android.ui.SharedViewModel
import com.example.garam.takemehome_android.ui.home.call_List
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.order_info_dialog.*
import kotlinx.android.synthetic.main.order_list.*

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private var lists = arrayListOf<order_List>()
    private lateinit var dialog : Dialog


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        notificationsViewModel =
            ViewModelProviders.of(this).get(NotificationsViewModel::class.java)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_notifications, container, false)
        val recycler = root.findViewById<RecyclerView>(R.id.orderRecycler)
        dialog = Dialog(root.context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.order_info_dialog)
        val sharedData = sharedViewModel.getData()

        for (i in 0  until sharedData.size)
        {
            lists.add(0, order_List(sharedData[i].storeName,sharedData[i].storeAddress,sharedData[i].finishTime))
        }

        val orderRecycler = OrderViewAdapter(lists,root.context){ orderList ->
            showDialog(orderList)
        }
        recycler.adapter = orderRecycler
        orderRecycler.notifyDataSetChanged()

        recycler.layoutManager = LinearLayoutManager(root.context)
        recycler.setHasFixedSize(true)

        return root
    }

    private fun showDialog(orderList: order_List){
        dialog.show()
        dialog.setCanceledOnTouchOutside(false)
        dialog.orderAddress.text = "주소 : ${orderList.storeAddress}"
        dialog.orderTime.text = "도착 예정 시간 : ${orderList.finishTime}"
        dialog.orderPrice.text = "가격 : $"
        dialog.orderMethod.text = "결제 수단 : "
        dialog.orderRequest.text = "요청 사항 : "

        dialog.order_info_confirm.setOnClickListener {
            dialog.dismiss()
        }
    }


}