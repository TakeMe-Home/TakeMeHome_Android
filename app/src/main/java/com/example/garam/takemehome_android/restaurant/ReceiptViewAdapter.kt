package com.example.garam.takemehome_android.restaurant

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.garam.takemehome_android.R

class ReceiptViewAdapter(
    private val items: ArrayList<ReceiptList>,
    val context: Context,
    private val itemClick:(ReceiptList)-> Unit) : RecyclerView.Adapter<ReceiptViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ReceiptViewAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.receipt_list_layout,parent
            ,false), itemClick)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ReceiptViewAdapter.ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(itemView: View, itemClick: (ReceiptList) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        fun bind(list: ReceiptList) {

            itemView.setOnClickListener {
                itemClick(list)
            }
        }
    }
}