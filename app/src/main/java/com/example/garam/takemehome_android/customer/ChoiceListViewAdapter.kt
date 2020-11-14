package com.example.garam.takemehome_android.customer

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.garam.takemehome_android.R

class ChoiceListViewAdapter(
    private val items: ArrayList<MenuChoiceList>,
    val context: Context): RecyclerView.Adapter<ChoiceListViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ChoiceListViewAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.choicelayout,parent,false))
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ChoiceListViewAdapter.ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    inner class ViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView){
        private val choiceMenu = itemView.findViewById<TextView>(R.id.choiceMenuName)
        private val choicePrice = itemView.findViewById<TextView>(R.id.choiceMenuPrice)
        private val choiceMiddlePrice = itemView.findViewById<TextView>(R.id.choiceMiddlePrice)

        fun bind(list: MenuChoiceList){
            choiceMenu.text = list.choiceMenuName
            choicePrice.text = list.choiceMenuPrice.toString()
            choiceMiddlePrice.text = list.choiceMiddlePrice.toString()
        }
    }
}