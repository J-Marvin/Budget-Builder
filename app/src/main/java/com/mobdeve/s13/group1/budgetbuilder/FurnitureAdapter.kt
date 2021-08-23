package com.mobdeve.s13.group1.budgetbuilder

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView

class FurnitureAdapter(
    private val fragmentManager: FragmentManager?,
    private val dataSet: ArrayList<Furniture>,
    context: Context) : RecyclerView.Adapter<FurnitureViewHolder>(){

    private val context = context
    private var activatedIndex = -1
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
            activatedIndex = position
        } else if (curFurniture.owned) {
            holder.hidePrice()
            holder.unequip()
        } else {
            holder.unequip()
        }

        holder.setOnClickListener(View.OnClickListener {
            // If no furniture is activated
            if (activatedIndex == -1 && curFurniture.owned) {
                curFurniture.equip()
                holder.equip()
            } else if (activatedIndex != position){ // If pressing on owned item

                if(curFurniture.owned) {
                    curFurniture.equip()
                    holder.equip()
                    dataSet[activatedIndex].equipped = false
                    notifyItemChanged(activatedIndex)
                    activatedIndex = position
                } else {
                    var dialog = PurchaseDialogFragment.newInstance(curFurniture)
                    dialog.show(fragmentManager!!, "purchaseItem_tag")
                }
            } else if(!curFurniture.owned){
                var dialog = PurchaseDialogFragment.newInstance(curFurniture)
                dialog.show(fragmentManager!!, "purchaseItem_tag")
            }
        })
    }

    override fun getItemCount() = dataSet.size

    private fun findActivated(): Int {

        for(i in 0 until dataSet.size)
            if(dataSet[i].equipped)
                return i

        return -1
    }
}