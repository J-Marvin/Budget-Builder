package com.mobdeve.s13.group1.budgetbuilder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class GalleryViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val ivRoom: ImageView
    val tvName: TextView

    init {
        ivRoom = view.findViewById(R.id.iv_room)
        tvName = view.findViewById(R.id.tv_room_name)
    }

    fun setImg(img: Int) {
        ivRoom.setImageResource(img)
    }

    fun setName(name: String) {
        tvName.text = name
    }
}