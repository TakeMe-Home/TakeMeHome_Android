package com.example.garam.takemehome_android.customer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.garam.takemehome_android.R

class MenuListViewAdapter(
    private val items: ArrayList<MenuList>,
    val context: Context,
    val itemClick:(MenuList)-> Unit): RecyclerView.Adapter<MenuListViewAdapter.ViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MenuListViewAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.menu_list,parent,false)
            ,itemClick)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MenuListViewAdapter.ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(itemView: View, itemClick: (MenuList) -> Unit) : RecyclerView.ViewHolder(itemView){
        private val menuName = itemView.findViewById<TextView>(R.id.menuNameTextView)
        private val menuPrice = itemView.findViewById<TextView>(R.id.menuPriceTextView)
        private val menuStatus = itemView.findViewById<TextView>(R.id.menuStatusTextView)

        fun bind(list: MenuList){
            menuName.text = list.menuName
            menuPrice.text = list.menuPrice.toString() + "원"
            when {
                list.menuStatus == "SALE" ->{
                    menuStatus.text = "판매 중"
                }
                list.menuStatus == "SOLD_OUT" ->{
                    menuStatus.text = "품절"
                }
            }

            itemView.setOnClickListener {
                itemClick(list)
            }
        }
    }
}