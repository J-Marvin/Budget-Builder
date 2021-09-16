package com.mobdeve.s13.group1.budgetbuilder.dao

/** This interface represents a DataAccessObject for the Expense Table */
interface ExpenseDAO {
    /** This method adds an expense
     *  @param expense - the expense to be added
     *  @return returns the row_id of the expense
     * */
    fun addExpense(expense: ExpenseModel): Long

    /**
     * This method finds all the expenses between two dates (inclusive)
     * @param start - the start date (yyyy-MM-dd)
     * @param end - the end date (yyyy-MM-dd)
     * @param ascending - represents whether the data should be in ascending order
     * @return returns an arraylist of expenses
     * */
    fun getExpensesByDate(start: String, end: String?, ascending: Boolean?): ArrayList<ExpenseModel>

    /**
     * This method finds all the expenses
     * @return returns an arraylist of expenses
     * */
    fun getAllExpenses(): ArrayList<ExpenseModel>

    /** This method updates the row of an expense
     *  @param expense - the expense containing the updated data (also contains the row_id)
     *  @return returns true if the record has been updated. Otherwise, returns false
     * */
    fun updateExpense(expense: ExpenseModel): Boolean

    /** This method deletes an Expense
     *  @param rowId - the row_id of the expense to be deleted
     *  @return returns true if the record has been deleted. Otherwise, returns false
     * */
    fun deleteExpense(rowId: String): Boolean
}