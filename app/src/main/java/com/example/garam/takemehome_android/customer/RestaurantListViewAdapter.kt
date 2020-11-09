package com.example.garam.takemehome_android.customer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.garam.takemehome_android.R

class RestaurantListViewAdapter(
    private val items: ArrayList<RestaurantList>,
    val context: Context,
    val itemClick:(RestaurantList)-> Unit) : RecyclerView.Adapter<RestaurantListViewAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent:ViewGroup, viewType:Int): RestaurantListViewAdapter.ViewHolder{
        return ViewHolder(
            LayoutInflater.from(context).inflate(R.layout.restaurant_list,parent,false),
            itemClick
        )
    }
    override fun getItemCount(): Int{
        return items.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position:Int){
        holder.bind(items[position])
    }

    inner class ViewHolder(itemView: View, itemClick:(RestaurantList) -> Unit): RecyclerView.ViewHolder(itemView){

        private val restaurantName = itemView.findViewById<TextView>(R.id.restaurantNameTextView)
        private val restaurantAddress = itemView.findViewById<TextView>(R.id.restaurantAddressTextView)
        private val restaurantPhone = itemView.findViewById<TextView>(R.id.restaurantPhoneTextView)

        fun bind(list: RestaurantList){
            restaurantName.text = list.restaurantName
            restaurantAddress.text = list.restaurantAddress
            restaurantPhone.text = list.restaurantPhone

            itemView.setOnClickListener {
                itemClick(list)
            }
        }
    }
}
