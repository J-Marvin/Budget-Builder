package com.mobdeve.s13.group1.budgetbuilder

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

class ExpenseViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val ivExpenseItemPic: ImageView
    val tvType: TextView
    val tvDesc: TextView
    val tvExpenseAmount: TextView
    val tvTime: TextView

    init {
        ivExpenseItemPic = view.findViewById(R.id.iv_expense_item_pic)
        tvType = view.findViewById(R.id.tv_expense_type)
        tvDesc = view.findViewById(R.id.tv_expense_desc)
        tvExpenseAmount = view.findViewById(R.id.tv_expense_amount)
        tvTime = view.findViewById(R.id.tv_expense_item_time)
    }

    fun setType(type: String) {
        //set pic
        if(type.equals("Shopping", true)) {
            ivExpenseItemPic.setImageResource(R.drawable.shopping_basket)
            ivExpenseItemPic.setColorFilter(Color.parseColor("#FB7414"))
            ivExpenseItemPic.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#F6C376"))
        }
        else if(type.equals("Food", true)) {
            ivExpenseItemPic.setImageResource(R.drawable.restaurant)
            ivExpenseItemPic.setColorFilter(Color.parseColor("#CF3B3B"))
            ivExpenseItemPic.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#EDC4C4"))
        }

        else if(type.equals("Transportation", true)) {
            ivExpenseItemPic.setImageResource(R.drawable.car)
            ivExpenseItemPic.setColorFilter(Color.parseColor("#FF6200EE"))
            ivExpenseItemPic.backgroundTintList = ColorStateList.valueOf(Color.parseColor("#FFD7B8FB"))
        }

        //set type text
        tvType.text = type
    }

    fun setDesc(desc: String) {
        tvDesc.text = desc
    }

    fun setAmount(amount: Int) {
        tvExpenseAmount.text = "-" + amount.toString()
    }

    fun setTime(currTime: Date) {
        var currTimeText = SimpleDateFormat("hh:mm a").format(currTime)
        tvTime.text = currTimeText
    }

}