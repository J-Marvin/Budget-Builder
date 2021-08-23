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

class CategoryExpenseViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val tvExpense = view.tv_category_expense_name
    val pbProgress = view.pb_expense
    val tvTotal = view.tv_category_expense_total
    val ivColor = view.iv_category_color

    fun setExpense(total: Float, currency: Char) {
        tvTotal.text = "-$currency$total"
    }

    fun setProgress(percent: Int) {
        pbProgress.progress = percent
    }

    fun setCategory(category: String) {
        tvExpense.text = category
    }

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