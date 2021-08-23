package com.mobdeve.s13.group1.budgetbuilder

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*

class BudgetBuilderDbHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object{
        private const val DATABASE_NAME = "BudgetBuilder.db"
        private const val DATABASE_VERSION = 1
        const val ROOM_TABLE = "rooms"
        private const val COLUMN_ROOM_ID = "room_id"
        private const val COLUMN_ROOM_MONTH = "month"
        private const val COLUMN_ROOM_YEAR = "year"

        const val FURNITURE_TABLE = "furniture"
        private const val COLUMN_FURNITURE_ID = "furniture_id"
        private const val COLUMN_FURNITURE_TYPE = "furniture_type"
        private const val COLUMN_FURNITURE_NAME = "name"
        private const val COLUMN_FURNITURE_PRICE = "price"
        private const val COLUMN_FURNITURE_EQUIPPED = "equipped"
        private const val COLUMN_FURNITURE_OWNED = "owned"
        private const val COLUMN_FURNITURE_IMG = "image"

        const val BUDGET_TABLE = "budgets"
        private const val COLUMN_BUDGET_ID = "budget_id"
        private const val COLUMN_BUDGET_AMOUNT = "budget_amount"
        private const val COLUMN_BUDGET_DATE = "budget_date"

        const val EXPENSE_TABLE = "expenses"
        private const val COLUMN_EXPENSE_ID = "expense_id"
        private const val COLUMN_EXPENSE_TYPE = "expense_type"
        private const val COLUMN_EXPENSE_AMOUNT = "expense_amount"
        private const val COLUMN_EXPENSE_DESC = "expense_desc"
        private const val COLUMN_EXPENSE_DATE = "date"

        const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
        val dateFormatterComplete = SimpleDateFormat(DATE_FORMAT)
        const val DATE_NO_TIME_FORMAT = "yyyy-MM-dd"
        val dateFormatterNoTime = SimpleDateFormat(DATE_NO_TIME_FORMAT)
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
                "$COLUMN_FURNITURE_IMG INTEGER NOT NULL," +
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
                "$COLUMN_EXPENSE_DESC TEXT NOT NULL," +
                "$COLUMN_EXPENSE_DATE TEXT NOT NULL," +
                "$COLUMN_BUDGET_ID INTEGER NOT NULL," +
                "FOREIGN KEY($COLUMN_BUDGET_ID) REFERENCES $BUDGET_TABLE($COLUMN_BUDGET_ID)" +
                ")"

        db?.execSQL(createRoomTable)
        db?.execSQL(createFurnitureTable)
        db?.execSQL(createBudgetTable)
        db?.execSQL(createExpenseTable)

        initDb(db)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $EXPENSE_TABLE")
        db?.execSQL("DROP TABLE IF EXISTS $BUDGET_TABLE")
        db?.execSQL("DROP TABLE IF EXISTS $FURNITURE_TABLE")
        db?.execSQL("DROP TABLE IF EXISTS $ROOM_TABLE")
        this.onCreate(db)
    }

    fun initDb(db: SQLiteDatabase?){
        var furnitures = DataHelper.getFurniture()
        var expenses = DataHelper.getExpenses()
        var today = Date()

        var roomId = addRoom(db, today.month, today.year)
        var budgetId = addBudget(db,20000F, today.toString())

        if (roomId != -1L) {
            for(furniture in furnitures){
                furniture.roomId = roomId.toString()
                addFurniture(db, furniture)
            }

            for(expense in expenses) {
                expense.budgetId = budgetId.toString()
                addExpense(db, expense)
            }
        }
    }

    fun addRoom(month: Int, year: Int): Long {
        val db = writableDatabase
        val cv = ContentValues()

        cv.put(COLUMN_ROOM_MONTH, month)
        cv.put(COLUMN_ROOM_YEAR, year)

        val result = db.insert(ROOM_TABLE, null, cv)

        if (result == -1L) {
            Toast.makeText(this.context, "Failed to insert room", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this.context, "Successfully inserted room", Toast.LENGTH_SHORT).show()
        }

        return result
    }

    fun addRoom(db: SQLiteDatabase?, month: Int, year: Int): Long? {
        val cv = ContentValues()

        cv.put(COLUMN_ROOM_MONTH, month)
        cv.put(COLUMN_ROOM_YEAR, year)

        val result = db?.insert(ROOM_TABLE, null, cv)

        if (result == -1L) {
            Toast.makeText(this.context, "Failed to insert room", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this.context, "Successfully inserted room", Toast.LENGTH_SHORT).show()
        }

        return result
    }

    fun addFurniture(furniture: Furniture): Long{
        val db = writableDatabase
        val cv = ContentValues()

        cv.put(COLUMN_ROOM_ID, furniture.roomId)
        cv.put(COLUMN_FURNITURE_TYPE, furniture.type)
        cv.put(COLUMN_FURNITURE_NAME, furniture.name)
        cv.put(COLUMN_FURNITURE_PRICE, furniture.price)
        cv.put(COLUMN_FURNITURE_EQUIPPED, if (furniture.equipped) 1 else 0)
        cv.put(COLUMN_FURNITURE_OWNED, if(furniture.owned) 1 else 0)
        cv.put(COLUMN_FURNITURE_IMG, furniture.imageId)

        val result = db.insert(FURNITURE_TABLE, null, cv)

        if (result == -1L) {
            Toast.makeText(this.context, "Failed to insert Furniture", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this.context, "Successfully inserted ${furniture.name}", Toast.LENGTH_SHORT).show()
        }

        return result
    }

    fun addFurniture(db: SQLiteDatabase?, furniture: Furniture): Long?{
        val cv = ContentValues()

        cv.put(COLUMN_ROOM_ID, furniture.roomId)
        cv.put(COLUMN_FURNITURE_TYPE, furniture.type)
        cv.put(COLUMN_FURNITURE_NAME, furniture.name)
        cv.put(COLUMN_FURNITURE_PRICE, furniture.price)
        cv.put(COLUMN_FURNITURE_EQUIPPED, if (furniture.equipped) 1 else 0)
        cv.put(COLUMN_FURNITURE_OWNED, if(furniture.owned) 1 else 0)
        cv.put(COLUMN_FURNITURE_IMG, furniture.imageId)

        val result = db?.insert(FURNITURE_TABLE, null, cv)

        if (result == -1L) {
            Toast.makeText(this.context, "Failed to insert Furniture", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this.context, "Successfully inserted Furniture", Toast.LENGTH_SHORT).show()
        }

        return result
    }

    fun addExpense(expense: Expense): Long{
        val db = writableDatabase
        val cv = ContentValues()

        cv.put(COLUMN_EXPENSE_TYPE, expense.type)
        cv.put(COLUMN_EXPENSE_AMOUNT, expense.amount)
        cv.put(COLUMN_EXPENSE_DATE, expense.date.toString())
        cv.put(COLUMN_EXPENSE_DESC, expense.desc)
        cv.put(COLUMN_BUDGET_ID, expense.budgetId)

        val result = db.insert(EXPENSE_TABLE, null, cv)

        if (result == -1L) {
            Toast.makeText(this.context, "Failed to insert Expense", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this.context, "Successfully inserted Expense", Toast.LENGTH_SHORT).show()
        }

        return result
    }

    fun addExpense(db: SQLiteDatabase?, expense: Expense): Long?{
        val cv = ContentValues()

        cv.put(COLUMN_EXPENSE_TYPE, expense.type)
        cv.put(COLUMN_EXPENSE_AMOUNT, expense.amount)
        cv.put(COLUMN_EXPENSE_DATE, dateFormatterComplete.format(expense.date))
        cv.put(COLUMN_EXPENSE_DESC, expense.desc)
        cv.put(COLUMN_BUDGET_ID, expense.budgetId)

        val result = db?.insert(EXPENSE_TABLE, null, cv)

        if (result == -1L) {
            Toast.makeText(this.context, "Failed to insert Expense", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this.context, "Successfully inserted Expense", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(this.context, "Failed to insert Budget", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this.context, "Successfully inserted Budget", Toast.LENGTH_SHORT).show()
        }

        return result
    }

    fun addBudget(db: SQLiteDatabase?, amount: Float, date: String): Long? {
        val cv = ContentValues()

        cv.put(COLUMN_BUDGET_AMOUNT, amount)
        cv.put(COLUMN_BUDGET_DATE, date)

        val result = db?.insert(BUDGET_TABLE, null, cv)

        if (result == -1L) {
            Toast.makeText(this.context, "Failed to insert Budget", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this.context, "Successfully inserted Budget", Toast.LENGTH_SHORT).show()
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

    fun findAllFurniture(): ArrayList<Furniture> {
        val query = "SELECT * FROM $FURNITURE_TABLE"
        val db = this.readableDatabase

        var cursor: Cursor? = null

        if (db!= null) {
            cursor = db.rawQuery(query, null)
        }

        val data = ArrayList<Furniture>()

        if (cursor != null) {
            cursor.moveToFirst()
            do {
                val furniture = Furniture(
                    cursor.getInt(cursor.getColumnIndex(COLUMN_FURNITURE_IMG)),
                    cursor.getInt(cursor.getColumnIndex(COLUMN_FURNITURE_PRICE)),
                    owned = cursor.getInt(cursor.getColumnIndex(COLUMN_FURNITURE_OWNED)) == 1,
                    equipped = cursor.getInt(cursor.getColumnIndex(COLUMN_FURNITURE_EQUIPPED)) == 1,
                    cursor.getString(cursor.getColumnIndex(COLUMN_FURNITURE_NAME)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_FURNITURE_TYPE))
                )

                furniture.roomId = cursor.getInt(cursor.getColumnIndex(COLUMN_ROOM_ID)).toString()
                furniture.furnitureId = cursor.getInt(cursor.getColumnIndex(COLUMN_FURNITURE_ID)).toString()

                data.add(furniture)
            } while (cursor.moveToNext())

            cursor.close()
        }

        return data
    }

    fun findAllExpensesBetween(start: String, end: String): ArrayList<Expense> {
        val query = "SELECT * " +
                "FROM $EXPENSE_TABLE " +
                "WHERE $COLUMN_EXPENSE_DATE BETWEEN \'$start\' AND \'$end\'"

        val db = readableDatabase

        var cursor: Cursor? = null

        if (db != null) {
            cursor = db.rawQuery(query, null)
        }

        val data = ArrayList<Expense>()

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val expense = Expense(
                    dateFormatterComplete.parse(cursor.getString(cursor.getColumnIndex(
                        COLUMN_EXPENSE_DATE)))!!,
                    cursor.getString(cursor.getColumnIndex(COLUMN_EXPENSE_TYPE)),
                    cursor.getFloat(cursor.getColumnIndex(COLUMN_EXPENSE_AMOUNT)),
                    cursor.getString(cursor.getColumnIndex(COLUMN_EXPENSE_DESC))
                )

                expense.budgetId = cursor.getInt(cursor.getColumnIndex(COLUMN_BUDGET_ID)).toString()
                expense.expenseId = cursor.getInt(cursor.getColumnIndex(COLUMN_EXPENSE_ID)).toString()

                data.add(expense)

            } while (cursor.moveToNext())

            cursor.close()
        }

        return data
    }

    fun updateFurniture(furniture: Furniture): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues();

        cv.put(COLUMN_FURNITURE_TYPE, furniture.type)
        cv.put(COLUMN_FURNITURE_NAME, furniture.name)
        cv.put(COLUMN_FURNITURE_PRICE, furniture.price)
        cv.put(COLUMN_FURNITURE_EQUIPPED, if(furniture.equipped) 1 else 0)
        cv.put(COLUMN_FURNITURE_OWNED, if(furniture.owned) 1 else 0)

        val result = db.update(FURNITURE_TABLE, cv, "$COLUMN_FURNITURE_ID=?", arrayOf(furniture.roomId))

        if (result == -1) {
            Toast.makeText(this.context, "Failed to update Furniture", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this.context, "Updated Furniture", Toast.LENGTH_SHORT).show()
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