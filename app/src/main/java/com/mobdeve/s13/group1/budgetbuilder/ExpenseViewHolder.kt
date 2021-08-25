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
    val tvDate: TextView
    val tvTime: TextView

    init {
        ivExpenseItemPic = view.findViewById(R.id.iv_expense_item_pic)
        tvType = view.findViewById(R.id.tv_expense_type)
        tvDesc = view.findViewById(R.id.tv_expense_desc)
        tvExpenseAmount = view.findViewById(R.id.tv_expense_amount)
        tvDate = view.findViewById(R.id.tv_expense_item_date)
        tvTime = view.findViewById(R.id.tv_expense_item_time)
    }

    fun setType(type: String) {
        var expenseType = ExpenseType.valueOf(type.uppercase()) //find enum value

        //set icon
        ivExpenseItemPic.setImageResource(expenseType.iconImg)
        ivExpenseItemPic.setColorFilter(Color.parseColor(expenseType.iconColor))
        ivExpenseItemPic.backgroundTintList = ColorStateList.valueOf(Color.parseColor(expenseType.backColor))

        //set type text
        tvType.text = expenseType.textType
    }

    fun setDesc(desc: String) {
        tvDesc.text = desc
    }

    fun setAmount(amount: Float) {
        tvExpenseAmount.text = "-" + amount.toString()
    }

    fun setDateTime(currTime: Date) {
        tvDate.text = SimpleDateFormat("MMM dd, yyyy").format(currTime)
        tvTime.text = SimpleDateFormat("hh:mm a").format(currTime)
    }

}