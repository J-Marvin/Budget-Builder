package com.mobdeve.s13.group1.budgetbuilder

import android.opengl.Visibility
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FurnitureViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val ivFurniture: ImageView
    val ivCoin: ImageView
    val ivCheck: ImageView
    val tvPrice: TextView

    init {
        ivFurniture = view.findViewById(R.id.iv_furniture)
        tvPrice = view.findViewById(R.id.tv_price)
        ivCoin = view.findViewById(R.id.iv_furniture_coin)
        ivCheck = view.findViewById(R.id.iv_furniture_check)
    }

    fun setImg(img: Int) {
        ivFurniture.setImageResource(img)
    }

    fun setPrice(price: Int) {
        tvPrice.text = price.toString()
    }

    fun hidePrice() {
        ivCoin.visibility = View.GONE
        tvPrice.visibility = View.GONE
    }

    fun equip() {
        ivCheck.visibility = View.VISIBLE
    }

    fun unequip() {
        ivCheck.visibility = View.GONE
    }
}