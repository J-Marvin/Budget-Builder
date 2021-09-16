package com.mobdeve.s13.group1.budgetbuilder

import com.mobdeve.s13.group1.budgetbuilder.dao.ExpenseModel

/**
 * This interface updates the expense item view
 */
interface UpdateExpenseHandler {

    /**
     * This function updates the expense item view after editing
     * @param expense the updated expense object
     */
    fun updateExpenseView(expense: ExpenseModel)
}