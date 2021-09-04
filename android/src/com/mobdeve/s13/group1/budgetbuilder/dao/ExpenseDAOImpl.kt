package com.mobdeve.s13.group1.budgetbuilder.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.badlogic.gdx.utils.Null
import com.mobdeve.s13.group1.budgetbuilder.FormatHelper
import com.mobdeve.s13.group1.budgetbuilder.Keys
import java.util.HashMap

class ExpenseDAOImpl(context: Context): ExpenseDAO {

    companion object{
        /** This method adds an expense to a given database
         *  @param db - the database
         *  @param expenseModel - the expense to be added
         *  @return returns the row_id of the database
         * */
        fun addExpense(db: SQLiteDatabase?, expenseModel: ExpenseModel): Long? {
            val cv = ContentValues()

            cv.put(DbReferences.COLUMN_EXPENSE_TYPE, expenseModel.type)
            cv.put(DbReferences.COLUMN_EXPENSE_AMOUNT, expenseModel.amount)
            cv.put(
                DbReferences.COLUMN_EXPENSE_DATE,
                FormatHelper.dateFormatterComplete.format(expenseModel.date)
            )
            cv.put(DbReferences.COLUMN_EXPENSE_DESC, expenseModel.desc)
            cv.put(DbReferences.COLUMN_BUDGET_ID, expenseModel.budgetId)

            return db?.insert(DbReferences.EXPENSE_TABLE, null, cv)
        }
    }

    private val db: BudgetBuilderDbHelper

    init {
        db = BudgetBuilderDbHelper.getInstance(context)
    }

    /** This method adds an expense
     *  @param expense - the expense to be added
     *  @return returns the row_id of the expense
     * */
    override fun addExpense(expense: ExpenseModel): Long {
        val db = db.writableDatabase
        val cv = ContentValues()

        cv.put(DbReferences.COLUMN_EXPENSE_TYPE, expense.type)
        cv.put(DbReferences.COLUMN_EXPENSE_AMOUNT, expense.amount)
        cv.put(
            DbReferences.COLUMN_EXPENSE_DATE,
            FormatHelper.dateFormatterComplete.format(expense.date)
        )
        cv.put(DbReferences.COLUMN_EXPENSE_DESC, expense.desc)
        cv.put(DbReferences.COLUMN_BUDGET_ID, expense.budgetId)

        return db.insert(DbReferences.EXPENSE_TABLE, null, cv)
    }

    /**
     * This method finds all the expenses between two dates (inclusive)
     * @param start - the start date (yyyy-MM-dd)
     * @param end - the end date (yyyy-MM-dd)
     * @param ascending - represents whether the data should be in ascending order
     * @return returns an arraylist of expenses
     * */
    override fun getExpensesByDate(start: String, end: String?, ascending: Boolean?): ArrayList<ExpenseModel> {
        val db = db.readableDatabase
        val endDate = end ?: start
        var cursor: Cursor? = null

        Log.d("getExpensedByDate", "Start: $start\nEnd: $endDate")
        if (db != null) {
            cursor = if (ascending == true) {
                db.rawQuery(DbReferences.FIND_EXPENSES_BY_DATE_ASC, arrayOf(start, endDate))
            } else {
                db.rawQuery(DbReferences.FIND_EXPENSES_BY_DATE_DESC, arrayOf(start,
                    endDate
                ))
            }
        }

        val data = java.util.ArrayList<ExpenseModel>()

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val expense = ExpenseModel(
                    FormatHelper.dateFormatterComplete.parse(cursor.getString(cursor.getColumnIndex(
                        DbReferences.COLUMN_EXPENSE_DATE
                    )))!!,
                    cursor.getString(cursor.getColumnIndex(DbReferences.COLUMN_EXPENSE_TYPE)),
                    cursor.getFloat(cursor.getColumnIndex(DbReferences.COLUMN_EXPENSE_AMOUNT)),
                    cursor.getString(cursor.getColumnIndex(DbReferences.COLUMN_EXPENSE_DESC))
                )

                expense.budgetId = cursor.getInt(cursor.getColumnIndex(DbReferences.COLUMN_BUDGET_ID)).toString()
                expense.expenseId = cursor.getInt(cursor.getColumnIndex(DbReferences.COLUMN_EXPENSE_ID)).toString()

                data.add(expense)

            } while (cursor.moveToNext())

            cursor.close()
        }

        return data
    }

    override fun getAllExpenses(): ArrayList<ExpenseModel> {
        val db = db.readableDatabase
        var cursor: Cursor? = null

        if (db != null) {
            cursor = db.rawQuery(DbReferences.FIND_ALL_BUDGET, null)
        }

        val data = java.util.ArrayList<ExpenseModel>()

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val expense = ExpenseModel(
                    FormatHelper.dateFormatterComplete.parse(cursor.getString(cursor.getColumnIndex(
                        DbReferences.COLUMN_EXPENSE_DATE
                    )))!!,
                    cursor.getString(cursor.getColumnIndex(DbReferences.COLUMN_EXPENSE_TYPE)),
                    cursor.getFloat(cursor.getColumnIndex(DbReferences.COLUMN_EXPENSE_AMOUNT)),
                    cursor.getString(cursor.getColumnIndex(DbReferences.COLUMN_EXPENSE_DESC))
                )

                expense.budgetId = cursor.getInt(cursor.getColumnIndex(DbReferences.COLUMN_BUDGET_ID)).toString()
                expense.expenseId = cursor.getInt(cursor.getColumnIndex(DbReferences.COLUMN_EXPENSE_ID)).toString()

                data.add(expense)

            } while (cursor.moveToNext())

            cursor.close()
        }

        return data
    }

    /** This method updates the row of an expense
     *  @param expense - the expense containing the updated data (also contains the row_id)
     *  @return returns true if the record has been updated. Otherwise, returns false
     * */
    override fun updateExpense(expense: ExpenseModel): Boolean {
        val db = db.writableDatabase
        val cv = ContentValues()

        cv.put(DbReferences.COLUMN_EXPENSE_TYPE, expense.type)
        cv.put(DbReferences.COLUMN_EXPENSE_AMOUNT, expense.amount)
        cv.put(DbReferences.COLUMN_EXPENSE_DESC, expense.desc)

        val result = db.update(DbReferences.EXPENSE_TABLE, cv, "${DbReferences.COLUMN_EXPENSE_ID}=?", arrayOf(expense.expenseId))

        return result != -1
    }


    /** This method deletes an Expense
     *  @param rowId - the row_id of the expense to be deleted
     *  @return returns true if the record has been deleted. Otherwise, returns false
     * */
    override fun deleteExpense(rowId: String):Boolean {
        val db = db.writableDatabase
        val result = db.delete(DbReferences.EXPENSE_TABLE, "${DbReferences.COLUMN_EXPENSE_ID}=?", arrayOf(rowId))

        return result != -1
    }

    /** This method returns a Cursor given the arguments for an aggregation statement for the expense table
     *  @param start - the start date (yyyy-MM-dd)
     *  @param end - the end date (yyyy-MM-dd)
     *  @param columns - the ArrayList<String> of column names
     *  @param agg - an ArrayList containing HashMaps which map the name, type and alias of a typical sqlite statement
     *  @param groups - the ArrayList of column names for the data to be grouped
     *  @param orderColumn - the column where the sorting will be based on
     *  @param orderType - the type of ordering
     *  @param limit - the number or rows to be taken
     *  @return returns a Cursor for the data
     * */
    fun getAggExpensesBetween(
        start: String,
        end: String,
        columns: java.util.ArrayList<String>?,
        agg: java.util.ArrayList<HashMap<String, String>>,
        groups: java.util.ArrayList<String>,
        orderColumn: String?,
        orderType: String?,
        limit: Int?
    ): Cursor? {
        var query = StringBuilder()
        val db = db.readableDatabase
        query.append("SELECT ")
        var count = 0

        if (columns != null) {
            for (column in columns) {
                if (count > 0)
                    query.append(", ")

                query.append(column)
                count++
            }
        }

        for (hashmap in agg) {
            if(
                hashmap.containsKey(Keys.KEY_COLUMN_NAME.toString()) &&
                hashmap.containsKey(Keys.KEY_COLUMN_ALIAS.toString()) &&
                hashmap.containsKey(Keys.KEY_AGG_TYPE.toString())
            ) {
                if (count > 0)
                    query.append(", ")
                query.append(hashmap[Keys.KEY_AGG_TYPE.toString()]).append("(")
                    .append(hashmap[Keys.KEY_COLUMN_NAME.toString()]).append(") \"")
                    .append(hashmap[Keys.KEY_COLUMN_ALIAS.toString()]).append("\" ")
                count++

            }
        }

        count = 0

        query.append("FROM ${DbReferences.EXPENSE_TABLE} ")
        query.append("WHERE ${DbReferences.COLUMN_EXPENSE_DATE} BETWEEN \'$start\' AND \'$end\' ")

        if (groups.size > 0)
            query.append("GROUP BY ")
        for (group in groups) {
            if (count > 0)
                query.append(", ")
            query.append(group).append(" ")
        }

        if (orderColumn != null && orderType != null) {
            query.append("ORDER BY ")
                .append(orderColumn)
                .append(" ")
                .append(orderType)
        }

        if (limit != null) {
            query.append("LIMIT $limit")
        }
        var cursor: Cursor? = null
        if (db != null) {
            cursor = db.rawQuery(query.toString(), null)
        }

        return cursor
    }

    /**
     * This method finds the 3 latest expenses
     * @return returns an arraylist of the 3 latest expenses
     * */
    fun getRecentExpenses(): java.util.ArrayList<ExpenseModel> {
        val query = "SELECT * " +
                "FROM ${DbReferences.EXPENSE_TABLE} " +
                "ORDER BY ${DbReferences.COLUMN_EXPENSE_DATE} DESC " +
                "LIMIT 3"

        val db = db.readableDatabase

        var cursor: Cursor? = null

        if (db != null) {
            cursor = db.rawQuery(query, null)
        }

        val data = java.util.ArrayList<ExpenseModel>()

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val expense = ExpenseModel(
                    FormatHelper.dateFormatterComplete.parse(cursor.getString(cursor.getColumnIndex(
                        DbReferences.COLUMN_EXPENSE_DATE
                    )))!!,
                    cursor.getString(cursor.getColumnIndex(DbReferences.COLUMN_EXPENSE_TYPE)),
                    cursor.getFloat(cursor.getColumnIndex(DbReferences.COLUMN_EXPENSE_AMOUNT)),
                    cursor.getString(cursor.getColumnIndex(DbReferences.COLUMN_EXPENSE_DESC))
                )

                expense.budgetId = cursor.getInt(cursor.getColumnIndex(DbReferences.COLUMN_BUDGET_ID)).toString()
                expense.expenseId = cursor.getInt(cursor.getColumnIndex(DbReferences.COLUMN_EXPENSE_ID)).toString()

                data.add(expense)

            } while (cursor.moveToNext())

            cursor.close()
        }

        return data
    }
}