package com.mobdeve.s13.group1.budgetbuilder

/** This interface represents a handler for adding an expense*/
interface ExpenseHandler {

    /** This method is executed when the expense is added
     *  @param amount - the amount of the expense
     *  @param type - the category of the expense
     *  */
    fun onAddExpense(amount: Float, type: String)

    /** This method is executed when the expense is not added
     * */
    fun onCancelExpense()
}