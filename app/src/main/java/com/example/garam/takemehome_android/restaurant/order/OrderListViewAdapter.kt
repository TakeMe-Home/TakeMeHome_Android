package com.example.garam.takemehome_android.restaurant.order

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.garam.takemehome_android.R
import com.example.garam.takemehome_android.restaurant.AllOrderListDataClass

class OrderListViewAdapter(
    private val items: ArrayList<AllOrderListDataClass>,
    val context: Context,
    private val itemClick:(AllOrderListDataClass) -> Unit ) : RecyclerView.Adapter<OrderListViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.order_list_layout,parent,false)
        ,itemClick)

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])

    }

    inner class ViewHolder(itemView: View, itemClick: (AllOrderListDataClass) -> Unit):
            RecyclerView.ViewHolder(itemView){

        private val orderPrice = itemView.findViewById<TextView>(R.id.orderListTotalPrice)
        private val orderAddress = itemView.findViewById<TextView>(R.id.orderListAddress)
        private val orderCustomerPhone = itemView.findViewById<TextView>(R.id.orderListCustomerPhone)
        private val orderPaymentStatus = itemView.findViewById<TextView>(R.id.orderListPaymentStatus)
        private val orderPaymentType = itemView.findViewById<TextView>(R.id.orderListPaymentType)

        fun bind(list: AllOrderListDataClass) {

            orderAddress.text = list.customerAddress
            orderPrice.text = list.totalPrice.toString() + "원"
            orderCustomerPhone.text = list.customerPhone

            when(list.paymentStatus){
                "COMPLITE" -> {
                    orderPaymentStatus.text = "결제완료"
                }
                "NONE" -> {
                    orderPaymentStatus.text = "미결제"
                    when(list.paymentType){
                        "CARD" -> {
                            orderPaymentType.text = "카드"
                        }
                        "CASH" -> {
                            orderPaymentType.text = "현금"
                        }
                    }
                }

            }

            itemView.setOnClickListener {
                itemClick(list)
            }
        }
    }

}