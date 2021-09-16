package com.mobdeve.s13.group1.budgetbuilder

import android.graphics.drawable.Drawable
import android.opengl.Visibility
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

/** This class represents a view holder for the recycler view for furnitures*/
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

    /** This methods sets the image resource
     *  @param img - the resource id of the image
     * */
    fun setImg(img: Int) {
        ivFurniture.setImageResource(img)
    }

    /** This method sets the image drawable
     *  @param drawable - the Drawable
     * */
    fun setImg(drawable: Drawable) {
        ivFurniture.setImageDrawable(drawable)
    }

    /** This method sets the price of the furniture
     *  @param price - the price
     * */
    fun setPrice(price: Int) {
        tvPrice.text = price.toString()
    }

    /** This method hides the price */
    fun hidePrice() {
        ivCoin.visibility = View.GONE
        tvPrice.visibility = View.GONE
    }

    /** This method shows the check mark */
    fun equip() {
        ivCheck.visibility = View.VISIBLE
    }

    /** This method hides the check mark */
    fun unequip() {
        ivCheck.visibility = View.GONE
    }

    /** This method sets the OnClickListener
     *  @param listener - the View.OnClickListener*/
    fun setOnClickListener(listener: View.OnClickListener){
        ivFurniture.setOnClickListener(listener)
    }
}