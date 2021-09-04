package com.mobdeve.s13.group1.budgetbuilder

import com.mobdeve.s13.group1.budgetbuilder.dao.ExpenseModel

interface SendFragmentData {
    fun refreshAddExpenseAdapter(expenseModel: ExpenseModel)
    fun sendExpenseAdapter(expenseAdapter: ExpenseAdapter, fragmentCaller: String)
}