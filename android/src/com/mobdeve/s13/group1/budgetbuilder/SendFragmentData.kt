package com.mobdeve.s13.group1.budgetbuilder

interface SendFragmentData {
    fun refreshExpenseAdapter(expense: Expense)
    fun sendExpenseAdapter(expenseAdapter: ExpenseAdapter, fragmentCaller: String)
}