package com.example.garam.takemehome_android.ui.home

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.os.persistableBundleOf
import androidx.recyclerview.widget.RecyclerView
import com.example.garam.takemehome_android.R
import kotlin.properties.Delegates

class CallViewAdapter (
    val items: ArrayList<call_List>,
    val context: Context,
    val itemClick: (call_List)-> Unit) : RecyclerView.Adapter<CallViewAdapter.ViewHolder>(){

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

    inner class ViewHolder(itemView: View , itemClick: (call_List) -> Unit): RecyclerView.ViewHolder(itemView) {

        private val storeName = itemView.findViewById<TextView>(R.id.storeName)
        private val storeAddress = itemView.findViewById<TextView>(R.id.storeAddress)
        private val finishTime = itemView.findViewById<TextView>(R.id.finishTime)
        private var position: Int? = null
        fun bind(list: call_List) {
            storeName.text = list.storeName
            storeAddress.text = list.storeAddress
            finishTime.text = list.finishTime

            itemView.setOnClickListener {
                itemClick(list)
            }

        }
    }

}