package com.example.garam.takemehome_android.ui.order

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
        private val storeName = itemView?.findViewById<TextView>(R.id.orderstoreName)
        private val storeAddress = itemView?.findViewById<TextView>(R.id.orderstoreAddress)
        private val finishTime = itemView?.findViewById<TextView>(R.id.orderfinishTime)

        fun bind(list:OrderList){
            storeName.text = list.storeName
            storeAddress.text = list.storeAddress
            finishTime.text = list.finishTime

            itemView.setOnClickListener { itemClick(list) }
        }

    }
}
