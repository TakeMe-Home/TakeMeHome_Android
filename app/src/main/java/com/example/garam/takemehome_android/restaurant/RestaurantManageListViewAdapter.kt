package com.example.garam.takemehome_android.restaurant

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.garam.takemehome_android.R

class RestaurantManageListViewAdapter(
    private val items: ArrayList<RestaurantManageList>,
    val context:Context,
    private val itemClick:(RestaurantManageList) -> Unit):
    RecyclerView.Adapter<RestaurantManageListViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RestaurantManageListViewAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(
            R.layout.restaurant_manage_recycler_layout,parent,false),itemClick)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(
        holder: ViewHolder,
        position: Int
    ) {
        holder.bind(items[position])
    }

    inner class ViewHolder(itemView: View, itemClick:(RestaurantManageList)-> Unit)
        : RecyclerView.ViewHolder(itemView){
        private val manageRestaurantName = itemView.findViewById<TextView>(R.id.restaurantNameManage)

        fun bind(list: RestaurantManageList){
            manageRestaurantName.text = list.restaurantName

            itemView.setOnClickListener {
                 itemClick(list)
            }
            itemView.isLongClickable = true

        }
    }
}