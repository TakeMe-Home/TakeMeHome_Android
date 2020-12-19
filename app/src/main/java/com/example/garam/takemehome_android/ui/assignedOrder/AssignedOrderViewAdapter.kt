package com.example.garam.takemehome_android.ui.assignedOrder

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.garam.takemehome_android.R

class AssignedOrderViewAdapter(
    private val items: ArrayList<AssignedOrderList>,
    val context: Context,
    private val itemClick:(AssignedOrderList) -> Unit) : RecyclerView.Adapter<AssignedOrderViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AssignedOrderViewAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.assigned_order_list_layout,
        parent,false),itemClick)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: AssignedOrderViewAdapter.ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(itemView: View, itemClick: (AssignedOrderList) -> Unit) : RecyclerView.ViewHolder(itemView){

        private val storeName = itemView.findViewById<TextView>(R.id.assignedStoreName)
        private val storeAddress = itemView.findViewById<TextView>(R.id.assignedStoreAddress)
        private val deliveryAddress = itemView.findViewById<TextView>(R.id.assignedCustomerAddress)
        private val deliveryPrice = itemView.findViewById<TextView>(R.id.assignedDeliveryPrice)

        fun bind(list: AssignedOrderList) {
            storeName.text = list.storeName
            storeAddress.text = list.storeAddress
            deliveryAddress.text = list.deliveryAddress
            deliveryPrice.text = list.deliveryPrice.toString() + "원"

            itemView.setOnClickListener{
                itemClick(list)
            }


        }
    }

}