package com.example.garam.takemehome_android.ui.notifications

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.garam.takemehome_android.R
import com.example.garam.takemehome_android.ui.SharedViewModel
import kotlinx.android.synthetic.main.order_info_dialog.*

class NotificationsFragment : Fragment() {

    private lateinit var notificationsViewModel: NotificationsViewModel
    private lateinit var sharedViewModel: SharedViewModel
    private var lists = arrayListOf<OrderList>()
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
            lists.add(0, OrderList(sharedData[i].storeName,
                sharedData[i].storeAddress,sharedData[i].finishTime))
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

    private fun showDialog(orderList: OrderList){
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