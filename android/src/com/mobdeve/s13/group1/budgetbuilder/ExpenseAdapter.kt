package com.mobdeve.s13.group1.budgetbuilder

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import java.text.SimpleDateFormat

class ExpenseAdapter(
    private val fragmentManager: FragmentManager?,
    var dataSet: ArrayList<Expense>) : RecyclerView.Adapter<ExpenseViewHolder>(){
    lateinit var view: View

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {

        view = LayoutInflater.from(parent.context).inflate(R.layout.expense_item, parent, false)
        val holder = ExpenseViewHolder(view)
        return holder
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        var currExpense = dataSet[position]

        holder.setType(currExpense.type)
        holder.setDesc(currExpense.desc)
        holder.setAmount(currExpense.amount)
        holder.setDateTime(currExpense.date)

        holder.setOnClickListener(View.OnClickListener {
            var words = currExpense.type.split(" ")
            var dateText = SimpleDateFormat("MMM dd, yyyy hh:mm a").format(currExpense.date)

            val args = Bundle()
            args.putFloat(Keys.KEY_VIEW_EXPENSE_AMOUNT.toString(), currExpense.amount)
            args.putString(Keys.KEY_VIEW_EXPENSE_TYPE.toString(), words[0])
            args.putString(Keys.KEY_VIEW_EXPENSE_DESC.toString(), currExpense.desc)
            args.putString(Keys.KEY_VIEW_EXPENSE_DATE.toString(), dateText)

            Navigation.findNavController(view).navigate(R.id.action_global_viewExpenseItemFragment, args)

        })
    }

    override fun getItemCount(): Int{
        return dataSet.size
    }

}