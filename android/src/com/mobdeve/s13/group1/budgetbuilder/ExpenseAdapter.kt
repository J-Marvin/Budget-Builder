package com.mobdeve.s13.group1.budgetbuilder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.s13.group1.budgetbuilder.dao.ExpenseModel
import java.text.SimpleDateFormat

/** This class is an adapter for the expense list recycler view*/
class ExpenseAdapter(
    private val fragmentManager: FragmentManager?,
    var dataSet: ArrayList<ExpenseModel>) : RecyclerView.Adapter<ExpenseViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {

        var view = LayoutInflater.from(parent.context).inflate(R.layout.expense_item, parent, false)
        val holder = ExpenseViewHolder(view)

        // set on click listener to show the expense
        holder.setOnClickListener(View.OnClickListener {
            var currExpense = dataSet[holder.bindingAdapterPosition]
            var words = currExpense.type?.split(" ")
            var dateText = SimpleDateFormat("MMM dd, yyyy hh:mm a").format(currExpense.date)

            val args = Bundle()
            args.putFloat(Keys.KEY_VIEW_EXPENSE_AMOUNT.name, currExpense.amount!!)
            args.putString(Keys.KEY_VIEW_EXPENSE_TYPE.name, words!![0])
            args.putString(Keys.KEY_VIEW_EXPENSE_DESC.name, currExpense.desc)
            args.putString(Keys.KEY_VIEW_EXPENSE_DATE.name, dateText)
            args.putString(Keys.KEY_VIEW_EXPENSE_ID.name, currExpense.expenseId)

            Navigation.findNavController(view).navigate(R.id.action_global_viewExpenseItemFragment, args)
        })

        return holder
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        var currExpense = dataSet[position]

        holder.setType(currExpense.type!!)
        holder.setDesc(currExpense.desc!!)
        holder.setAmount(currExpense.amount!!)
        holder.setDateTime(currExpense.date!!)
    }

    override fun getItemCount(): Int{
        return dataSet.size
    }

}