package com.mobdeve.s13.group1.budgetbuilder

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat
import java.util.*

/** This class represents the view holder for the recycler view for expenses
 * */
class ExpenseViewHolder(view: View): RecyclerView.ViewHolder(view) {
    val ivExpenseItemPic: ImageView
    val tvType: TextView
    val tvDesc: TextView
    val tvExpenseAmount: TextView
    val tvDate: TextView
    val tvTime: TextView
    val cardExpense: CardView

    init {
        ivExpenseItemPic = view.findViewById(R.id.iv_expense_item_pic)
        tvType = view.findViewById(R.id.tv_expense_type)
        tvDesc = view.findViewById(R.id.tv_expense_desc)
        tvExpenseAmount = view.findViewById(R.id.tv_expense_amount)
        tvDate = view.findViewById(R.id.tv_expense_item_date)
        tvTime = view.findViewById(R.id.tv_expense_item_time)
        cardExpense = view.findViewById(R.id.card_expense_item)
    }

    /** This method sets the category of the expense
     *  @param type - the category of the expense
     *  */
    fun setType(type: String) {
        var expenseType = ExpenseType.valueOf(type.uppercase()) //find enum value

        //set icon
        ivExpenseItemPic.setImageResource(expenseType.iconImg)
        ivExpenseItemPic.setColorFilter(Color.parseColor(expenseType.iconColor))
        ivExpenseItemPic.backgroundTintList = ColorStateList.valueOf(Color.parseColor(expenseType.backColor))

        //set type text
        tvType.text = expenseType.textType
    }

    /** This method sets the description of the expense
     *  @param desc - the description of the expense
     *  */
    fun setDesc(desc: String) {
        tvDesc.text = desc
    }

    /** This method sets the amount of the expense
     *  @param amount - the amount of the expense
     *  */
    fun setAmount(amount: Float) {
        tvExpenseAmount.text = "-" + String.format("%.02f", amount)
    }

    /** This method sets the time of the expense
     *  @param currTime - the time of the expense
     *  */
    fun setDateTime(currTime: Date) {
        tvDate.text = SimpleDateFormat("MMM dd, yyyy").format(currTime)
        tvTime.text = SimpleDateFormat("hh:mm a").format(currTime)
    }

    /** This method sets the on click listener
     *  @param listener - the View.OnClickListener
     *  */
    fun setOnClickListener(listener: View.OnClickListener){
        cardExpense.setOnClickListener(listener)
    }

}