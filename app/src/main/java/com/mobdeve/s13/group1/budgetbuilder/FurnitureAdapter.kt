package com.mobdeve.s13.group1.budgetbuilder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class FurnitureAdapter(private val dataSet: ArrayList<Furniture>) : RecyclerView.Adapter<FurnitureViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FurnitureViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.furniture_shop_item, parent, false)
        val holder = FurnitureViewHolder(view)
        // attach onclicklistener here
        return holder
    }

    override fun onBindViewHolder(holder: FurnitureViewHolder, position: Int) {
        var curFurniture = dataSet[position]

        holder.setImg(curFurniture.imageId)
        holder.setPrice(curFurniture.price)

        if (curFurniture.equipped) {
            holder.hidePrice()
            holder.equip()
        } else if (curFurniture.owned) {
            holder.hidePrice()
            holder.unequip()
        } else {
            holder.unequip()
        }
    }

    override fun getItemCount() = dataSet.size
}