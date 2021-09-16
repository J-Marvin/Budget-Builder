package com.mobdeve.s13.group1.budgetbuilder

/** This interface represents a handler for setting the budget
 * */
interface BudgetHandler {

    /** The method to be executed after the confirming of setting the budget
     *  @param budget - the budget
     * */
    fun okBudget(budget: Float)

    /** The method to be executed when the cancel budget is clicked*/
    fun cancelBudget()
}