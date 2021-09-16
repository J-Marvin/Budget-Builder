package com.mobdeve.s13.group1.budgetbuilder

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/** This class represents the view holder for the recycler view for the rooms*/
class GalleryViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val ivRoom: ImageView
    val tvName: TextView

    init {
        ivRoom = view.findViewById(R.id.iv_room)
        tvName = view.findViewById(R.id.tv_room_name)
    }

    /** This method sets the image resource
     *  @param img - the resource id of the image*/
    fun setImg(img: Int) {
        ivRoom.setImageResource(img)
    }

    /** This method sets the image drawable
     *  @param drawable - the Drawable
     * */
    fun setImg(drawable:Drawable){
        ivRoom.setImageDrawable(drawable)
    }

    /** This method sets the name of the room
     *  @param name - the name of the room
     * */
    fun setName(name: String) {
        tvName.text = name
    }

    /** This method sets the OnClickListener
     *  @param listener - View.OnClickListener
     * */
    fun setOnClickListener(listener: View.OnClickListener) {
        ivRoom.setOnClickListener(listener)
    }
}