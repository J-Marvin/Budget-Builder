package com.mobdeve.s13.group1.budgetbuilder

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class GalleryViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val ivRoom: ImageView

    init {
        ivRoom = view.findViewById(R.id.iv_gallery_room)
    }

    fun setImg(img: Int) {
        ivRoom.setImageResource(img)
    }
}