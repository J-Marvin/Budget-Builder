package com.mobdeve.s13.group1.budgetbuilder

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CategoryExpenseAdapter(private val dataSet: ArrayList<CategoryExpense>, context: Context, var currency: String): RecyclerView.Adapter<CategoryExpenseViewHolder>() {
    private val context = context

    init{
        dataSet.sortByDescending {
            it.total
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryExpenseViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.category_expense_item, parent, false)

        return CategoryExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryExpenseViewHolder, position: Int) {
        val currExpense: CategoryExpense = dataSet[position]

        holder.setCategory(currExpense.type)
        holder.setProgress(currExpense.percent)
        holder.setCategoryColor(currExpense.color, context)
        holder.setExpense(currExpense.total, currency[0])
    }

    override fun getItemCount() = dataSet.size
}