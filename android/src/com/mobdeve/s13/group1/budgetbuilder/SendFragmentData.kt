package com.mobdeve.s13.group1.budgetbuilder

import com.mobdeve.s13.group1.budgetbuilder.dao.ExpenseModel

/**
 * This interface sends the expense recycler view data to the main activity and updates it
 */
interface SendFragmentData {
    /**
     * This function updates the expense list recycler view if a new expense is added
     * @param expenseModel the expense added
     */
    fun refreshAddExpenseAdapter(expenseModel: ExpenseModel)

    /**
     * This function sends the ExpenseAdapter object and the caller to the main activity
     * @param expenseAdapter the ExpenseAdapter associated with the recycler view
     * @param fragmentCaller the fragment that called the adapter
     */
    fun sendExpenseAdapter(expenseAdapter: ExpenseAdapter, fragmentCaller: String)
}