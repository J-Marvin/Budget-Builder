package com.mobdeve.s13.group1.budgetbuilder

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.group1.budgetbuilder.dao.FurnitureDAOImpl
import com.mobdeve.s13.group1.budgetbuilder.dao.FurnitureModel
import java.io.IOException

class FurnitureAdapter(
    private val fragmentManager: FragmentManager?,
    private val dataSet: ArrayList<FurnitureModel>,
    context: Context) : RecyclerView.Adapter<FurnitureViewHolder>(){

    private val context = context
    private var activatedIndex = -1
    private val furnitureDAOImpl = FurnitureDAOImpl(context)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FurnitureViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.furniture_shop_item, parent, false)
        val holder = FurnitureViewHolder(view)

        return holder
    }

    override fun onBindViewHolder(holder: FurnitureViewHolder, position: Int) {
        var curFurniture = dataSet[position]

        try {
            val ims = context.assets.open(curFurniture.path)
            holder.setImg(Drawable.createFromStream(ims, null))
            ims.close()
        } catch (ex: IOException) {
            holder.setImg(curFurniture.imageId)
        }
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
                activatedIndex = position
                notifyItemChanged(activatedIndex)

                furnitureDAOImpl.updateFurniture(curFurniture)
                //reload room
                fragmentManager?.commit{
                    replace<RoomFragment>(R.id.fcv_shop_room)
                }

            } else if (activatedIndex != position){ // If pressing on owned item

                if(curFurniture.owned) {
                    curFurniture.equip()
                    holder.equip()
                    dataSet[activatedIndex].unequip()

                    //update db unequipped furniture
                    furnitureDAOImpl.updateFurniture(dataSet[activatedIndex])

                    notifyItemChanged(activatedIndex)
                    activatedIndex = position

                    //update db equipped furniture
                    furnitureDAOImpl.updateFurniture(curFurniture)

                    //reload room
                    fragmentManager?.commit{
                        replace<RoomFragment>(R.id.fcv_shop_room)
                    }

                    Toast.makeText(context, curFurniture.roomId, Toast.LENGTH_SHORT).show()
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