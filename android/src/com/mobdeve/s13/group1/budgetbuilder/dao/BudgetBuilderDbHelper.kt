package com.mobdeve.s13.group1.budgetbuilder.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.mobdeve.s13.group1.budgetbuilder.*
import com.mobdeve.s13.group1.budgetbuilder.dao.RoomDAOImpl.Companion.addRoom
import java.util.*

class BudgetBuilderDbHelper(context: Context?) : SQLiteOpenHelper(context, DbReferences.DATABASE_NAME, null, DbReferences.DATABASE_VERSION) {

    companion object{
        private var instance: BudgetBuilderDbHelper? = null

        @Synchronized
        fun getInstance(context: Context?): BudgetBuilderDbHelper {
            if (instance == null) {
                instance = BudgetBuilderDbHelper(context?.applicationContext)
            }
            return instance!!
        }
    }

    /** This method creates the database
     *  @param db the SQLiteDatabase
     * */
    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(DbReferences.CREATE_ROOM_TABLE)
        db?.execSQL(DbReferences.CREATE_BUDGET_TABLE)
        db?.execSQL(DbReferences.CREATE_EXPENSE_TABLE)
        db?.execSQL(DbReferences.CREATE_FURNITURE_TABLE)

        initDb(db)
    }

    /** This method recreates the database when the app upgrades
     *  @param db - the database
     *  @param oldVersion - the old version
     *  @param newVersion - the new version
     * */
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(DbReferences.DROP_EXPENSE_TABLE)
        db?.execSQL(DbReferences.DROP_BUDGET_TABLE)
        db?.execSQL(DbReferences.DROP_FURNITURE_TABLE)
        db?.execSQL(DbReferences.DROP_ROOM_TABLE)
        this.onCreate(db)
    }

    /** This method initializes the data in the database
     *  @param db - the database
     * */
    fun initDb(db: SQLiteDatabase?){
        var furnitures = DataHelper.getFurniture()
        var expenses = DataHelper.getExpenses()
        var today = Date()

        var roomId = addRoom(db, today.month, today.year)
        var budgetId = BudgetDAOImpl.addBudget(db,20000F, today.toString())

        if (roomId != -1L) {
            for(furniture in furnitures){
                furniture.roomId = roomId.toString()
                FurnitureDAOImpl.addFurniture(db, furniture)
            }

            for(expense in expenses) {
                expense.budgetId = budgetId.toString()
                ExpenseDAOImpl.addExpense(db, expense)
            }
        }
    }

    fun resetDb(): Boolean {
        val db = this.writableDatabase
        db?.execSQL(DbReferences.DROP_EXPENSE_TABLE)
        db?.execSQL(DbReferences.DROP_BUDGET_TABLE)
        db?.execSQL(DbReferences.DROP_FURNITURE_TABLE)
        db?.execSQL(DbReferences.DROP_ROOM_TABLE)
        this.onCreate(db)

        return true
    }
}