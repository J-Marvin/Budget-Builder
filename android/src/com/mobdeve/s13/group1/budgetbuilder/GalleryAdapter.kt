package com.mobdeve.s13.group1.budgetbuilder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class GalleryAdapter(private val dataSet: ArrayList<Room>): RecyclerView.Adapter<GalleryViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.room_item, parent, false)
        val holder = GalleryViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        var currRoom = dataSet[position]

        holder.setImg(currRoom.img)
        holder.setName(currRoom.name)
    }

    override fun getItemCount() = dataSet.size

}
