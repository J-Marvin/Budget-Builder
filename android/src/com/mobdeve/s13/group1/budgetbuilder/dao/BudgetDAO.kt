package com.mobdeve.s13.group1.budgetbuilder.dao

/** This interface represents a DataAccessObject for the Budget Table */
interface BudgetDAO {
    /** This method adds a budget to the database
     *  @param budget - the amount of the budget
     *  @param date - the date of the budget (yyyy-MM-dd)
     *  @return returns the row_id of the inserted budget in the database if successful. Otherwise, returns -1
     * */
    fun addBudget(budget: Float, date: String): Long

    /** This method returns the BudgetModel given a specific date
     *  @param date - the date of the budget (yyyy-MM-dd)
     *  @return returns the BudgetModel object containing the info of the budget if it exists. Otherwise, returns null
     * */
    fun getBudgetByDate(date: String): BudgetModel?

    /** This method returns the BudgetModel given a specific id
     *  @param id - the row_id of the budget
     *  @return returns the BudgetModel object containing the info of the budget if it exists. Otherwise, returns null
     * */
    fun getBudgetById(id: String): BudgetModel?
    /** This method returns an arraylist of BudgetModel objects containing the information of all the budgets in the database
     *  @return returns an arraylist of BudgetModel objects containing the information of all the budgets in the database
     * */
    fun getAllBudgets(): ArrayList<BudgetModel>

    /** This method updates the row of a budget
     *  @param id - the id of the budget
     *  @param budget - the updated amount of the budget
     *  @return returns true if the record has been updated. Otherwise, returns false
     * */
    fun updateBudget(id: String, budget: Float): Boolean

    /** This method deletes a Budget
     *  @param id - the row_id of the budget to be deleted
     *  @return returns true if the record has been deleted. Otherwise, returns false
     * */
    fun deleteBudget(id: String): Boolean
}