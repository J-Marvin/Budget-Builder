package com.mobdeve.s13.group1.budgetbuilder

interface ExpenseHandler {
    fun onAddExpense(amount: Float, type: String)
    fun onCancelExpense()
}