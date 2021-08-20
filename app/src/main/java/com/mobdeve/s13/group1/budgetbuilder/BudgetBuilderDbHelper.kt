package com.mobdeve.s13.group1.budgetbuilder

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import java.time.LocalDate

class BudgetBuilderDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    private companion object{
        const val DATABASE_NAME = "BudgetBuilder.db"
        const val DATABASE_VERSION = 1
        const val ROOM_TABLE = "rooms"
        const val COLUMN_ROOM_ID = "room_id"
        const val COLUMN_ROOM_MONTH = "month"
        const val COLUMN_ROOM_YEAR = "year"

        const val FURNITURE_TABLE = "furniture"
        const val COLUMN_FURNITURE_ID = "furniture_id"
        const val COLUMN_FURNITURE_TYPE = "furniture_type"
        const val COLUMN_FURNITURE_NAME = "name"
        const val COLUMN_FURNITURE_PRICE = "price"
        const val COLUMN_FURNITURE_EQUIPPED = "equipped"
        const val COLUMN_FURNITURE_OWNED = "owned"
        const val COLUMN_FURNITURE_IMG = "image"

        const val BUDGET_TABLE = "budgets"
        const val COLUMN_BUDGET_ID = "budget_id"
        const val COLUMN_BUDGET_AMOUNT = "budget_amount"
        const val COLUMN_BUDGET_DATE = "budget_date"

        const val EXPENSE_TABLE = "expenses"
        const val COLUMN_EXPENSE_ID = "expense_id"
        const val COLUMN_EXPENSE_TYPE = "expense_type"
        const val COLUMN_EXPENSE_AMOUNT = "expense_amount"
        const val COLUMN_EXPENSE_DATE = "date"
    }
    private val context = context

    override fun onCreate(db: SQLiteDatabase?) {
        val createRoomTable = "CREATE TABLE $ROOM_TABLE (" +
                "$COLUMN_ROOM_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_ROOM_MONTH INTEGER NOT NULL," +
                "$COLUMN_ROOM_YEAR INTEGER NOT NULL)"

        val createFurnitureTable = "CREATE TABLE $FURNITURE_TABLE (" +
                "$COLUMN_FURNITURE_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_ROOM_ID INTEGER NOT NULL," +
                "$COLUMN_FURNITURE_TYPE TEXT NOT NULL," +
                "$COLUMN_FURNITURE_NAME TEXT NOT NULL," +
                "$COLUMN_FURNITURE_PRICE INTEGER NOT NULL," +
                "$COLUMN_FURNITURE_EQUIPPED NUMERIC NOT NULL," +
                "$COLUMN_FURNITURE_OWNED NUMERIC NOT NULL," +
                "$COLUMN_FURNITURE_IMG INTEGER NOT NULL"
                "FOREIGN KEY($COLUMN_ROOM_ID) REFERENCES $ROOM_TABLE($COLUMN_ROOM_ID)" +
                ")"

        val createBudgetTable = "CREATE TABLE $BUDGET_TABLE (" +
                "$COLUMN_BUDGET_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_BUDGET_AMOUNT REAL NOT NULL," +
                "$COLUMN_BUDGET_DATE TEXT NOT NULL" +
                ")"

        val createExpenseTable = "CREATE TABLE $EXPENSE_TABLE (" +
                "$COLUMN_EXPENSE_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_EXPENSE_TYPE TEXT NOT NULL," +
                "$COLUMN_EXPENSE_AMOUNT REAL NOT NULL," +
                "$COLUMN_EXPENSE_DATE TEXT NOT NULL," +
                "$COLUMN_BUDGET_ID INTEGER NOT NULL," +
                "FOREIGN KEY($COLUMN_BUDGET_ID) REFERENCES $BUDGET_TABLE($COLUMN_BUDGET_ID)"
                ")"

        db?.execSQL(createRoomTable)
        db?.execSQL(createFurnitureTable)
        db?.execSQL(createBudgetTable)
        db?.execSQL(createExpenseTable)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $EXPENSE_TABLE")
        db?.execSQL("DROP TABLE IF EXISTS $BUDGET_TABLE")
        db?.execSQL("DROP TABLE IF EXISTS $FURNITURE_TABLE")
        db?.execSQL("DROP TABLE IF EXISTS $ROOM_TABLE")
        this.onCreate(db)
    }

    private fun initDb(){

    }

    fun addRoom(month: Int, year: Int): Long {
        val db = writableDatabase
        val cv = ContentValues()

        cv.put(COLUMN_ROOM_MONTH, month)
        cv.put(COLUMN_ROOM_YEAR, year)

        val result = db.insert(ROOM_TABLE, null, cv)

        if (result == -1L) {
            Toast.makeText(this.context, "Failed to insert room", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this.context, "Successfully inserted room", Toast.LENGTH_LONG).show()
        }

        return result
    }

    fun addFurniture(room: Int, type: String, name: String, price: Int, equipped: Boolean, owned: Boolean, ): Long{
        val db = writableDatabase
        val cv = ContentValues()

        cv.put(COLUMN_ROOM_ID, room)
        cv.put(COLUMN_FURNITURE_TYPE, type)
        cv.put(COLUMN_FURNITURE_NAME, name)
        cv.put(COLUMN_FURNITURE_PRICE, price)
        cv.put(COLUMN_FURNITURE_EQUIPPED, if (equipped) 1 else 0)
        cv.put(COLUMN_FURNITURE_OWNED, if(owned) 1 else 0)

        val result = db.insert(FURNITURE_TABLE, null, cv)

        if (result == -1L) {
            Toast.makeText(this.context, "Failed to insert Furniture", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this.context, "Successfully inserted Furniture", Toast.LENGTH_LONG).show()
        }

        return result
    }

    fun addExpense(type: String, amount: Float, date: String, budget: Int): Long {
        val db = writableDatabase
        val cv = ContentValues()

        cv.put(COLUMN_EXPENSE_TYPE, type)
        cv.put(COLUMN_EXPENSE_AMOUNT, amount)
        cv.put(COLUMN_EXPENSE_DATE, date)
        cv.put(COLUMN_BUDGET_ID, budget)

        val result = db.insert(EXPENSE_TABLE, null, cv)

        if (result == -1L) {
            Toast.makeText(this.context, "Failed to insert Expense", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this.context, "Successfully inserted Expense", Toast.LENGTH_LONG).show()
        }

        return result
    }

    fun addBudget(amount: Float, date: String): Long {
        val db = writableDatabase
        val cv = ContentValues()

        cv.put(COLUMN_BUDGET_AMOUNT, amount)
        cv.put(COLUMN_BUDGET_DATE, date)

        val result = db.insert(BUDGET_TABLE, null, cv)

        if (result == -1L) {
            Toast.makeText(this.context, "Failed to insert Budget", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this.context, "Successfully inserted Budget", Toast.LENGTH_LONG).show()
        }

        return result
    }

    fun findAll(table: String): Cursor? {
        val query = "SELECT * FROM $table"
        val db = this.readableDatabase

        var cursor: Cursor? = null

        if (db!= null) {
            cursor = db.rawQuery(query, null)
        }
        
        return cursor
    }

    fun updateFurniture(rowId: String, type: String, name: String, price: Float, equipped: Boolean, owned: Boolean): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues();

        cv.put(COLUMN_FURNITURE_TYPE, type)
        cv.put(COLUMN_FURNITURE_NAME, name)
        cv.put(COLUMN_FURNITURE_PRICE, price)
        cv.put(COLUMN_FURNITURE_EQUIPPED, if(equipped) 1 else 0)
        cv.put(COLUMN_FURNITURE_OWNED, if(owned) 1 else 0)

        val result = db.update(FURNITURE_TABLE, cv, "$COLUMN_FURNITURE_ID=?", arrayOf(rowId))

        if (result == -1) {
            Toast.makeText(this.context, "Failed to update Furniture", Toast.LENGTH_LONG).show()
        } else {
            Toast.makeText(this.context, "Updated Furniture", Toast.LENGTH_LONG).show()
        }

        return result != -1
    }

    fun updateBudget(rowId: String, amount: Float): Boolean{
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(COLUMN_BUDGET_AMOUNT, amount)

        val result = db.update(BUDGET_TABLE, cv, "$COLUMN_BUDGET_ID=?", arrayOf(rowId))

        return result != -1
    }

    fun updateExpense(rowId: String, type: String, amount: Float): Boolean{
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(COLUMN_EXPENSE_TYPE, type)
        cv.put(COLUMN_EXPENSE_AMOUNT, amount)

        val result = db.update(EXPENSE_TABLE, cv, "$COLUMN_EXPENSE_ID=?", arrayOf(rowId))

        return result != -1
    }

    fun deleteExpense(rowId: String):Boolean {
        val db = this.writableDatabase
        val result = db.delete(EXPENSE_TABLE, "$COLUMN_EXPENSE_ID=?", arrayOf(rowId))

        return result != -1
    }
}