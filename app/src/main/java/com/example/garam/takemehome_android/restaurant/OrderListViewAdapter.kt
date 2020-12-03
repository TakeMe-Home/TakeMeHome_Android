package com.example.garam.takemehome_android.restaurant

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.garam.takemehome_android.R

class OrderListViewAdapter(
    private val items: ArrayList<OrderList>,
    val context: Context,
    private val itemClick:(OrderList) -> Unit ) : RecyclerView.Adapter<OrderListViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderListViewAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.order_list_layout,parent,false)
        ,itemClick)

    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: OrderListViewAdapter.ViewHolder, position: Int) {
        holder.bind(items[position])

    }

    inner class ViewHolder(itemView: View, itemClick: (OrderList) -> Unit):
            RecyclerView.ViewHolder(itemView){

        private val orderPrice = itemView.findViewById<TextView>(R.id.orderListTotalPrice)
        private val orderAddress = itemView.findViewById<TextView>(R.id.orderListAddress)

        fun bind(list: OrderList) {

            orderAddress.text = list.deliveryAddress
            orderPrice.text = list.totalPrice.toString() + "원"

            itemView.setOnClickListener {
                itemClick(list)
            }
        }
    }

}