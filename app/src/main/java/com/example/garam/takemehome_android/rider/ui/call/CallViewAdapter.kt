package com.example.garam.takemehome_android.rider.ui.call

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.garam.takemehome_android.R

class CallViewAdapter (
    private val items: ArrayList<CallList>,
    val context: Context,
    val itemClick: (CallList)-> Unit) : RecyclerView.Adapter<CallViewAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CallViewAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.call_list,parent,false),
            itemClick
        )
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(itemView: View , itemClick: (CallList) -> Unit): RecyclerView.ViewHolder(itemView) {

        private val storeName = itemView.findViewById<TextView>(R.id.storeName)
        private val storeAddress = itemView.findViewById<TextView>(R.id.storeAddress)
        private val deliveryAddress = itemView.findViewById<TextView>(R.id.deliveryAddress)
        private val deliveryPrice = itemView.findViewById<TextView>(R.id.deliveryPrice)

        fun bind(list: CallList) {
            storeName.text = "가게 명 : ${list.storeName}"
            storeAddress.text = "가게 주소 : ${list.storeAddress}"
            deliveryAddress.text = "배달 주소 : ${list.deliveryAddress}"
            deliveryPrice.text = "배달료 : ${list.deliveryPrice.toString()}원"

            itemView.setOnClickListener {
                itemClick(list)
            }
        }
    }

}