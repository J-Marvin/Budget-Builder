package com.mobdeve.s13.group1.budgetbuilder

import com.mobdeve.s13.group1.budgetbuilder.dao.ExpenseModel

interface UpdateExpenseHandler {
    fun updateExpenseView(expense: ExpenseModel)
}