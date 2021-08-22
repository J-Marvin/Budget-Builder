package com.mobdeve.s13.group1.budgetbuilder

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView

class FurnitureAdapter(
    private val fragmentManager: FragmentManager?,
    private val dataSet: ArrayList<Furniture>,
    context: Context) : RecyclerView.Adapter<FurnitureViewHolder>(){

    private val context = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FurnitureViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.furniture_shop_item, parent, false)
        val holder = FurnitureViewHolder(view)

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

        holder.setOnClickListener(View.OnClickListener {
            if (curFurniture.equipped) {
                holder.unequip()
                curFurniture.equipped = false
            } else if (curFurniture.owned) {
                holder.equip()
                curFurniture.equipped = true
            } else {
                var dialog = PurchaseDialogFragment.newInstance(curFurniture)
                dialog.show(fragmentManager!!, "purchaseItem_tag")
            }

        })
    }

    override fun getItemCount() = dataSet.size
}