package com.example.garam.takemehome_android.rider.ui.order

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.garam.takemehome_android.R

class OrderViewAdapter (
    private val items: ArrayList<OrderList>,
    val context:Context,
    val itemClick:(OrderList) -> Unit) : RecyclerView.Adapter<OrderViewAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.order_list,parent,false),
            itemClick
        )
    }

    override fun getItemCount(): Int {
         return items.size
    }

    override fun onBindViewHolder(holder: OrderViewAdapter.ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(itemView: View, itemClick: (OrderList) -> Unit) : RecyclerView.ViewHolder(itemView){
        private val storeName = itemView.findViewById<TextView>(R.id.orderstoreName)
        private val storeAddress = itemView.findViewById<TextView>(R.id.orderstoreAddress)
        private val orderDeliveryAddress = itemView.findViewById<TextView>(R.id.orderDeliveryAddress)
        private val orderDeliveryPrice = itemView.findViewById<TextView>(R.id.orderDeliveryPrice)

        fun bind(list:OrderList){
            storeName.text = "가게 명 : " + list.storeName
            storeAddress.text = "가게 주소 : " + list.storeAddress
            orderDeliveryAddress.text = "배달 주소 : " + list.deliveryAddress
            orderDeliveryPrice.text = "배달료 : " + list.deliveryPrice.toString()

            itemView.setOnClickListener { itemClick(list) }
        }

    }
}
