package com.example.garam.takemehome_android.restaurant.pickup

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.garam.takemehome_android.R

class PickUpWaitListViewAdapter(
    private val items: ArrayList<PickUpWaitingList>,
    val context: Context,
    private val itemClick:(PickUpWaitingList) -> Unit) : RecyclerView.Adapter<PickUpWaitListViewAdapter.ViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PickUpWaitListViewAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.pick_up_list_layout,parent,false),
        itemClick)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PickUpWaitListViewAdapter.ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(itemView:View, itemClick: (PickUpWaitingList) -> Unit) : RecyclerView.ViewHolder(itemView){

        private val customerAddress = itemView.findViewById<TextView>(R.id.waitPickUpCustomerAddress)
        private val customerPhone = itemView.findViewById<TextView>(R.id.waitPickUpCustomerPhone)
        private val totalPrice = itemView.findViewById<TextView>(R.id.waitPickUpTotalPrice)
        private val paymentStatus = itemView.findViewById<TextView>(R.id.waitPickUpPaymentStatus)
        private val paymentType = itemView.findViewById<TextView>(R.id.waitPickUpPaymentType)

        fun bind (list: PickUpWaitingList){
            customerAddress.text = list.customerAddress
            customerPhone.text = list.customerPhone
            totalPrice.text = list.totalPrice.toString() + "원"

            when(list.paymentStatus){
                "COMPLITE" -> {
                    paymentStatus.text = "결제완료"
                }
                "NONE" -> {
                    paymentStatus.text = "미결제"
                    when(list.paymentType){
                        "CARD" -> {
                            paymentType.text = "카드"
                        }
                        "CASH" -> {
                            paymentType.text = "현금"
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