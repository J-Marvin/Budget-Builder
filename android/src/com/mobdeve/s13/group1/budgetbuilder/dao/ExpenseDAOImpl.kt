package com.mobdeve.s13.group1.budgetbuilder.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.DatabaseUtils
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import android.widget.Toast
import androidx.annotation.DrawableRes
import com.mobdeve.s13.group1.budgetbuilder.*
import com.mobdeve.s13.group1.budgetbuilder.dao.DbReferences.Companion.FIND_AVERAGE_PERFORMANCE_BY_MONTH
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.floor

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

    fun getSumOfDate(start: String, end: String?): Float {
        var sum = 0F
        val db = db.readableDatabase
        val cursor = db.rawQuery(DbReferences.FIND_SUM_OF_EXPENSES_BY_DATE, arrayOf(start, end?:start))

        if (cursor != null && cursor.moveToFirst()) {
            sum += cursor.getFloat(cursor.getColumnIndex(DbReferences.COLUMN_AGG_SUM))
        }

        cursor.close()

        return sum
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

    fun getAveragePerformanceOfMonth(month: Int, year:Int): Float {
        var avg = 0F
        val db = db.writableDatabase
        val start = DateHelper.getStartDateString(month ,year)
        val end = DateHelper.getEndDateString(month, year)
        var cursor = db.rawQuery(FIND_AVERAGE_PERFORMANCE_BY_MONTH, arrayOf(start, end))

        if (cursor != null && cursor.moveToFirst()) {

        }

        Log.d("CURSOR", DatabaseUtils.dumpCursorToString(cursor))
        return avg
    }

    fun getSumPerDayOfMonth(month: Int, year: Int): ArrayList<MonthlyExpense> {
        val startDate = Calendar.getInstance()
        val endDate = Calendar.getInstance()
        val db = db.readableDatabase
        startDate.apply {
            this.set(Calendar.MONTH, month)
            this.set(Calendar.YEAR, year)
            this.set(Calendar.DAY_OF_MONTH, 1)
        }

        endDate.apply {
            this.set(Calendar.MONTH, month)
            this.set(Calendar.YEAR, year)
            this.set(Calendar.DAY_OF_MONTH, this.getActualMaximum(Calendar.DAY_OF_MONTH))
            this.add(Calendar.DATE, 1)
        }

        val maxSize = startDate.getActualMaximum(Calendar.DAY_OF_MONTH)
        val data = ArrayList<MonthlyExpense>()
        Log.d("Max Size", maxSize.toString())

        for (i in 0 until maxSize) {
            val date = Calendar.getInstance()
            date.apply{
                set(Calendar.MONTH, month)
                set(Calendar.YEAR, year)
                set(Calendar.DAY_OF_MONTH, i + 1)
            }

            data.add(MonthlyExpense(date, 0F))
            Log.d("index", i.toString())
        }

        var cursor = db.rawQuery(DbReferences.FIND_DAILY_SUM_BY_MONTH,
            arrayOf(FormatHelper.dateFormatterNoTime.format(startDate.time),
            FormatHelper.dateFormatterNoTime.format(endDate.time)))

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val strDate = cursor.getString(cursor.getColumnIndex(DbReferences.COLUMN_EXPENSE_DATE))
                val expenseDate = Calendar.getInstance()
                expenseDate.time = FormatHelper.dateFormatterNoTime.parse(strDate)
                val expenseAmount = cursor.getFloat(cursor.getColumnIndex(DbReferences.COLUMN_AGG_SUM))

                data[expenseDate.get(Calendar.DAY_OF_MONTH) - 1].amount = expenseAmount
            } while (cursor.moveToNext())
        }
        return data
    }

    fun getSumOfCategoriesBetweenDate(start: String, end: String?): Float {
        var total = 0f
        val db = db.readableDatabase

        val cursor = db.rawQuery(DbReferences.GET_SUM_BY_CATEGORY, arrayOf(start, end?:start))

        if (cursor != null && cursor.moveToFirst())
            total = cursor.getFloat(cursor.getColumnIndex(DbReferences.COLUMN_AGG_SUM))

        return total
    }

    fun getSumPerCategoryBetweenDate(start: String, end: String?): ArrayList<CategoryExpense> {
        val data = ArrayList<CategoryExpense>()
        val db = db.readableDatabase
        val cursor = db.rawQuery(DbReferences.FIND_EXPENSES_BY_CATEGORY_BETWEEN_DATE,
            arrayOf(start, end?:start))

        val total = getSumOfCategoriesBetweenDate(start, end)

        val ENT_INDEX = 0
        val FOOD_INDEX = 1
        val TRANS_INDEX = 2
        val UTIL_INDEX = 3
        val PERS_INDEX = 4
        val MED_INDEX = 5
        val OTHERS_INDEX = 6

        data.add(CategoryExpense("Entertainment", R.color.category_entertainment))
        data.add(CategoryExpense("Food", R.color.category_food))
        data.add(CategoryExpense("Transportation", R.color.category_transportation))
        data.add(CategoryExpense("Utilities", R.color.category_utilities))
        data.add(CategoryExpense("Personal",  R.color.category_personal))
        data.add(CategoryExpense("Medical", R.color.category_medical))
        data.add(CategoryExpense("Others", R.color.category_others))

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val category = cursor.getString(cursor.getColumnIndex(DbReferences.COLUMN_EXPENSE_TYPE))

                val index: Int = when(category) {
                    ExpenseType.ENTERTAINMENT.textType -> ENT_INDEX
                    ExpenseType.FOOD.textType -> FOOD_INDEX
                    ExpenseType.MEDICAL.textType -> MED_INDEX
                    ExpenseType.PERSONAL.textType -> PERS_INDEX
                    ExpenseType.TRANSPORTATION.textType -> TRANS_INDEX
                    ExpenseType.UTILITIES.textType -> UTIL_INDEX
                    else -> OTHERS_INDEX
                }

                val expense = data[index]
                expense.total = cursor.getFloat(cursor.getColumnIndex(DbReferences.COLUMN_AGG_SUM))
                expense.percent =  floor((expense.total / total).toDouble() * 100).toInt()

            } while (cursor.moveToNext())
        }

        return data
    }
}