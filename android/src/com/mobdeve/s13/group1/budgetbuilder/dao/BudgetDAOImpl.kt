package com.mobdeve.s13.group1.budgetbuilder.dao

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

class BudgetDAOImpl(context: Context?): BudgetDAO {
    companion object{
        /** This method adds a budget to a given database
         *  @param db - the database
         *  @param amount - the amount of the budget
         *  @param date - the date of the budget (yyyy-MM-dd)
         *  @return returns the row_id of the database
         * */
        fun addBudget(db: SQLiteDatabase?, amount: Float, date: String): Long? {
            val cv = ContentValues()

            cv.put(DbReferences.COLUMN_BUDGET_AMOUNT, amount)
            cv.put(DbReferences.COLUMN_BUDGET_DATE, date)

            return db?.insert(DbReferences.BUDGET_TABLE, null, cv)
        }
    }

    private val db: BudgetBuilderDbHelper

    init {
        db = BudgetBuilderDbHelper.getInstance(context)
    }

    /** This method adds a budget to the database
     *  @param amount - the amount of the budget
     *  @param date - the date of the budget (yyyy-MM-dd)
     *  @return returns the row_id of the inserted budget in the database if successful. Otherwise, returns -1
     * */
    override fun addBudget(amount: Float, date: String): Long {
        val db = db.writableDatabase
        val cv = ContentValues()

        cv.put(DbReferences.COLUMN_BUDGET_AMOUNT, amount)
        cv.put(DbReferences.COLUMN_BUDGET_DATE, date)

        val result = db.insert(DbReferences.BUDGET_TABLE, null, cv)

        return result
    }

    /** This method adds a budget to the database
     *  @param budget - the BudgetModel object containing the amount and date of the budget
     *  @return returns the row_id of the inserted budget in the database if successful. Otherwise, returns -1
     * */
    fun addBudget(budget: BudgetModel): Long {
        return if (budget.budget != null && budget.date != null) {
            addBudget(budget.budget!!, budget.date!!)
        } else -1
    }

    /** This method returns the BudgetModel given a specific date
     *  @param date - the date of the budget (yyyy-MM-dd)
     *  @return returns the BudgetModel object containing the info of the budget if it exists. Otherwise, returns null
     * */
    override fun getBudgetByDate(date: String): BudgetModel? {
        val db = db.readableDatabase
        val cursor = db.rawQuery(DbReferences.FIND_BUDGET_BY_DATE, arrayOf(date))
        var budget: BudgetModel? = null
        if (cursor != null && cursor.moveToFirst()) {
            budget = BudgetModel(
                cursor.getFloat(cursor.getColumnIndex(DbReferences.COLUMN_BUDGET_AMOUNT)),
                cursor.getString(cursor.getColumnIndex(DbReferences.COLUMN_BUDGET_DATE)),
                cursor.getString(cursor.getColumnIndex(DbReferences.COLUMN_BUDGET_ID))
            )
        }
        cursor.close()
        return budget
    }
    /** This method returns the BudgetModel given a specific id
     *  @param id - the row_id of the budget
     *  @return returns the BudgetModel object containing the info of the budget if it exists. Otherwise, returns null
     * */
    override fun getBudgetById(id: String): BudgetModel? {
        val db = db.readableDatabase
        val cursor = db.rawQuery(DbReferences.FIND_BUDGET_BY_ID, arrayOf(id))
        var budget: BudgetModel? = null
        if (cursor != null && cursor.moveToFirst()) {
            budget = BudgetModel(
                cursor.getFloat(cursor.getColumnIndex(DbReferences.COLUMN_BUDGET_AMOUNT)),
                cursor.getString(cursor.getColumnIndex(DbReferences.COLUMN_BUDGET_DATE)),
                cursor.getString(cursor.getColumnIndex(DbReferences.COLUMN_BUDGET_ID))
            )
        }
        cursor.close()
        return budget
    }

    /** This method returns an arraylist of BudgetModel objects containing the information of all the budgets in the database
     *  @return returns an arraylist of BudgetModel objects containing the information of all the budgets in the database
     * */
    override fun getAllBudgets(): ArrayList<BudgetModel> {
        val db = db.readableDatabase
        val cursor = db.rawQuery(DbReferences.FIND_ALL_BUDGET, null)
        var budgets = ArrayList<BudgetModel>()

        if (cursor != null && cursor.moveToFirst()) {
            do {
                budgets.add(BudgetModel(
                    cursor.getFloat(cursor.getColumnIndex(DbReferences.COLUMN_BUDGET_AMOUNT)),
                    cursor.getString(cursor.getColumnIndex(DbReferences.COLUMN_BUDGET_DATE)),
                    cursor.getString(cursor.getColumnIndex(DbReferences.COLUMN_BUDGET_ID))
                ))
            } while (cursor.moveToNext())
        }

        cursor.close()
        return budgets
    }


    /** This method updates the row of a budget
     *  @param rowId - the id of the budget
     *  @param amount - the updated amount of the budget
     *  @return returns true if the record has been updated. Otherwise, returns false
     * */
    override fun updateBudget(rowId: String, amount: Float): Boolean{
        val db = db.writableDatabase
        val cv = ContentValues()

        cv.put(DbReferences.COLUMN_BUDGET_AMOUNT, amount)

        val result = db.update(DbReferences.BUDGET_TABLE, cv, "${DbReferences.COLUMN_BUDGET_ID}=?", arrayOf(rowId))

        return result != -1
    }

    /** This method deletes a Budget
     *  @param rowId - the row_id of the budget to be deleted
     *  @return returns true if the record has been deleted. Otherwise, returns false
     * */
    fun deleteBudget(rowId: String): Boolean {
        val db = db.writableDatabase
        val result = db.delete(DbReferences.BUDGET_TABLE, "${DbReferences.COLUMN_BUDGET_ID}=?", arrayOf(rowId))

        return result != -1
    }
}