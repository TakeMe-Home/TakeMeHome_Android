package com.example.garam.takemehome_android.restaurant.receipt

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.garam.takemehome_android.R

class ReceiptMenuListViewAdapter(
    private val items: ArrayList<ReceiptMenuList>,
    val context: Context): RecyclerView.Adapter<ReceiptMenuListViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(
            R.layout.receipt_menu_list_layout,parent,false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val receiptMenuName = itemView.findViewById<TextView>(R.id.receiptMenuNameTextView)
        private val receiptMenuCount = itemView.findViewById<TextView>(R.id.receiptMenuCountTextView)

        fun bind(list: ReceiptMenuList) {
            receiptMenuName.text = list.orderMenuName
            receiptMenuCount.text = list.count.toString() + "인분"
        }

    }
}