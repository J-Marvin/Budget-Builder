package com.mobdeve.s13.group1.budgetbuilder.dao

interface ExpenseDAO {
    fun addExpense(expense: ExpenseModel): Long
    fun getExpensesByDate(start: String, end: String?, ascending: Boolean?): ArrayList<ExpenseModel>
    fun getAllExpenses(): ArrayList<ExpenseModel>
    fun updateExpense(expense: ExpenseModel): Boolean
    fun deleteExpense(rowId: String): Boolean
}