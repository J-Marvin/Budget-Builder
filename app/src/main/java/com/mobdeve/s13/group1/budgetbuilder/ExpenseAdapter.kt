package com.mobdeve.s13.group1.budgetbuilder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class ExpenseAdapter(private val dataSet: ArrayList<Expense>) : RecyclerView.Adapter<ExpenseViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.expense_item, parent, false)
        val holder = ExpenseViewHolder(view)
        // attach onclicklistener here
        return holder
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        var currExpense = dataSet[position]

        holder.setType(currExpense.type)
        holder.setDesc(currExpense.desc)
        holder.setAmount(currExpense.amount)
        holder.setDateTime(currExpense.date)

    }

    override fun getItemCount(): Int{

        return dataSet.size
    }
}