package com.example.garam.takemehome_android.restaurant

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.garam.takemehome_android.R

class MenuStatusViewAdapter(
    private val items: ArrayList<MenuStatusList>,
    val context: Context,
    private val itemClick:(MenuStatusList) -> Unit ): RecyclerView.Adapter<MenuStatusViewAdapter.ViewHolder>()
{
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MenuStatusViewAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.menu_status_layout,parent
            ,false), itemClick)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: MenuStatusViewAdapter.ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(itemView:View, itemClick: (MenuStatusList) -> Unit):
            RecyclerView.ViewHolder(itemView){

        private val menuName = itemView.findViewById<TextView>(R.id.menuCurrentNameTextView)
        private val menuPrice = itemView.findViewById<TextView>(R.id.menuCurrentPriceTextView)
        private val menuStatus = itemView.findViewById<TextView>(R.id.menuCurrentStatusTextView)

        fun bind(list: MenuStatusList){

            menuName.text = list.menuName
            menuPrice.text = "${list.menuPrice} 원"

            when(list.menuStatus){
                "SOLD_OUT" -> {
                    menuStatus.text = "품절"
                }
                "SALE" -> {
                    menuStatus.text = "판매중"
                }
            }

            itemView.setOnClickListener {
                itemClick(list)
            }
        }
    }
}