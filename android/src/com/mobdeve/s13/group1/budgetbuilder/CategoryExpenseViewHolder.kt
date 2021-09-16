package com.mobdeve.s13.group1.budgetbuilder

import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.ShapeDrawable
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.category_expense_item.view.*

/** This class represents the view holder for the recycler view in the summary fragment
 * */
class CategoryExpenseViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val tvExpense = view.tv_category_expense_name
    val pbProgress = view.pb_expense
    val tvTotal = view.tv_category_expense_total
    val ivColor = view.iv_category_color

    /** This method updates the expense
     *  @param total - the total amount
     *  @param currency - the currency used
     * */
    fun setExpense(total: Float, currency: Char) {
        tvTotal.text = "-$currency" + String.format("%.02f", total)
    }

    /** This method updates the progress bar
     *  @param percent - the percent to be set
     * */
    fun setProgress(percent: Int) {
        pbProgress.progress = percent
    }

    /** This method updates the category
     *  @param category - the category
     * */
    fun setCategory(category: String) {
        tvExpense.text = category
    }

    /** This method updates the color of the progress bar
     *  @param color - the id of the color from the resources
     *  @param context - the application context
     * */
    fun setCategoryColor(color: Int, context: Context) {

        val progressDrawable = pbProgress.progressDrawable.mutate()
        val circle = ivColor.drawable.mutate()

        val layerDrawable = progressDrawable as LayerDrawable
        val progressBar = layerDrawable.getDrawable(1)
        progressBar.colorFilter =  PorterDuffColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_IN)
        circle.colorFilter = PorterDuffColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_IN)

        pbProgress.progressDrawable = progressDrawable
    }
}