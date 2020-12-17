package com.example.garam.takemehome_android.restaurant.receipt

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.garam.takemehome_android.R

class ReceiptViewAdapter(
    private val items: ArrayList<ReceiptList>,
    val context: Context,
    private val itemClick:(ReceiptList)-> Unit) : RecyclerView.Adapter<ReceiptViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.receipt_list_layout,parent
            ,false), itemClick)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(itemView: View, itemClick: (ReceiptList) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private val innerRecycler = itemView.findViewById<RecyclerView>(R.id.menuNameRecycler)
        private val menuItems = ArrayList<ReceiptMenuList>()
        private val orderAddress = itemView.findViewById<TextView>(R.id.orderCustomerAddress)
        private val orderPrice = itemView.findViewById<TextView>(R.id.orderTotalPrice)
        private val cancelButton = itemView.findViewById<Button>(R.id.receiptCancelButton)
        private val acceptButton = itemView.findViewById<Button>(R.id.receiptConfirmButton)

        fun bind(list: ReceiptList) {

            orderAddress.text = list.customerAddress
            orderPrice.text = list.totalPrice.toString() + "원"

            /*for (i in 0 until list.menuNameCount.size){
                menuItems.add(list.menuNameCount[i])
            } */


            val mAdapter = ReceiptMenuListViewAdapter(list.menuNameCount, context)
            innerRecycler.adapter = mAdapter
            val layoutManager = LinearLayoutManager(context)
            innerRecycler.layoutManager = layoutManager

            mAdapter.notifyDataSetChanged()
            innerRecycler.setHasFixedSize(true)

          /*  itemView.setOnClickListener {
                itemClick(list)
            } */

            acceptButton.setOnClickListener {
                itemClick(list)
            }

            cancelButton.setOnClickListener {
                itemClick(list)
            }
        }
    }
}