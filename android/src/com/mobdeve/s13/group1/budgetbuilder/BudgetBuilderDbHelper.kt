package com.mobdeve.s13.group1.budgetbuilder

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap

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
        const val COLUMN_EXPENSE_TYPE = "expense_type"
        const val COLUMN_EXPENSE_AMOUNT = "expense_amount"
        const val COLUMN_EXPENSE_DESC = "expense_desc"
        const val COLUMN_EXPENSE_DATE = "date"

        const val DATE_FORMAT = "yyyy-MM-dd HH:mm:ss"
        val dateFormatterComplete = SimpleDateFormat(DATE_FORMAT)
        const val DATE_NO_TIME_FORMAT = "yyyy-MM-dd"
        val dateFormatterNoTime = SimpleDateFormat(DATE_NO_TIME_FORMAT)

        const val AGG_AVG = "avg"
        const val AGG_COUNT = "count"
        const val AGG_GROUP_CONCAT = "group_concat"
        const val AGG_MAX = "max"
        const val AGG_MIN = "min"
        const val AGG_SUM = "sum"
        const val AGG_TOTAL = "total"

        private val aggList = arrayOf("avg", "count", "max", "min", "sum", "total")
    }

    private val context = context

    /** This method creates the database
     *  @param db the SQLiteDatabase
     * */
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

    /** This method recreates the database when the app upgrades
     *  @param db - the database
     *  @param oldVersion - the old version
     *  @param newVersion - the new version
     * */
    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $EXPENSE_TABLE")
        db?.execSQL("DROP TABLE IF EXISTS $BUDGET_TABLE")
        db?.execSQL("DROP TABLE IF EXISTS $FURNITURE_TABLE")
        db?.execSQL("DROP TABLE IF EXISTS $ROOM_TABLE")
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

    /** This method inserts a room in the database and initializes all the furniture
     *  @param month - the month for the room(1-based index)
     *  @param year - the year for the room
     *  @return returns the row_id of the room
     * */
    fun initRoomFurniture(month: Int, year: Int): Long {
        val db = writableDatabase
        val roomId = addRoom(month, year)
        val furniture = DataHelper.getFurniture()

        if (roomId != -1L) {
            for(item in furniture){
                item.roomId = roomId.toString()
                addFurniture(db, item)
            }
        }

        return roomId
    }

    /** This method inserts a room in the database
     *  @param month - the month for the room(1-based index)
     *  @param year - the year for the room
     *  @return returns the row_id of the room
     * */
    fun addRoom(month: Int, year: Int): Long {
        val db = writableDatabase
        val cv = ContentValues()

        cv.put(COLUMN_ROOM_MONTH, month)
        cv.put(COLUMN_ROOM_YEAR, year)

        val result = db.insert(ROOM_TABLE, null, cv)

        return result
    }

    /** This method inserts a room in the database
     *  @param db - the database
     *  @param month - the month for the room(1-based index)
     *  @param year - the year for the room
     *  @return returns the row_id of the room
     * */
    fun addRoom(db: SQLiteDatabase?, month: Int, year: Int): Long? {
        val cv = ContentValues()

        cv.put(COLUMN_ROOM_MONTH, month)
        cv.put(COLUMN_ROOM_YEAR, year)

        val result = db?.insert(ROOM_TABLE, null, cv)

        return result
    }

    /** This method inserts one furniture inside the database
     *  @param furniture - the furniture to be inserted
     *  @return returns the row_id of the furniture
     * */
    fun addFurniture(furniture: Furniture): Long{
        val db = writableDatabase
        val cv = ContentValues()

        cv.put(COLUMN_FURNITURE_TYPE, furniture.type)
        cv.put(COLUMN_FURNITURE_NAME, furniture.name)
        cv.put(COLUMN_FURNITURE_PRICE, furniture.price)
        cv.put(COLUMN_FURNITURE_EQUIPPED, if (furniture.equipped) 1 else 0)
        cv.put(COLUMN_FURNITURE_OWNED, if(furniture.owned) 1 else 0)
        cv.put(COLUMN_FURNITURE_IMG, furniture.imageId)

        val result = db.insert(FURNITURE_TABLE, null, cv)

        return result
    }

    /** This method adds one furniture to a given database
     *  @param db - the database
     *  @param furniture - the furniture to be inserted
     *  @return returns the row_id of the furniture
     * */
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

        return result
    }

    /** This method adds an expense
     *  @param expense - the expense to be added
     *  @return returns the row_id of the expense
     * */
    fun addExpense(expense: Expense): Long{
        val db = writableDatabase
        val cv = ContentValues()

        cv.put(COLUMN_EXPENSE_TYPE, expense.type)
        cv.put(COLUMN_EXPENSE_AMOUNT, expense.amount)
        cv.put(COLUMN_EXPENSE_DATE, dateFormatterComplete.format(expense.date))
        cv.put(COLUMN_EXPENSE_DESC, expense.desc)
//        cv.put(COLUMN_BUDGET_ID, expense.budgetId)

        /*TODO: remove once find curr budget id is implemented*/
        cv.put(COLUMN_BUDGET_ID, "1")

        val result = db.insert(EXPENSE_TABLE, null, cv)

        return result
    }

    /** This method adds an expense to a given database
     *  @param db - the database
     *  @param expense - the expense to be added
     *  @return returns the row_id of the database
     * */
    fun addExpense(db: SQLiteDatabase?, expense: Expense): Long?{
        val cv = ContentValues()

        cv.put(COLUMN_EXPENSE_TYPE, expense.type)
        cv.put(COLUMN_EXPENSE_AMOUNT, expense.amount)
        cv.put(COLUMN_EXPENSE_DATE, dateFormatterComplete.format(expense.date))
        cv.put(COLUMN_EXPENSE_DESC, expense.desc)
        cv.put(COLUMN_BUDGET_ID, expense.budgetId)

        val result = db?.insert(EXPENSE_TABLE, null, cv)

        return result
    }

    /** This method adds a budget to the database
     *  @param amount - the amount of the budget
     *  @param date - the date of the budget (yyyy-MM-dd)
     *  @return returns the row_id of the database
     * */
    fun addBudget(amount: Float, date: String): Long {
        val db = writableDatabase
        val cv = ContentValues()

        cv.put(COLUMN_BUDGET_AMOUNT, amount)
        cv.put(COLUMN_BUDGET_DATE, date)

        val result = db.insert(BUDGET_TABLE, null, cv)

        return result
    }

    /** This method adds a budget to a given database
     *  @param db - the database
     *  @param amount - the amount of the budget
     *  @param date - the date of the budget (yyyy-MM-dd)
     *  @return returns the row_id of the database
     * */
    fun addBudget(db: SQLiteDatabase?, amount: Float, date: String): Long? {
        val cv = ContentValues()

        cv.put(COLUMN_BUDGET_AMOUNT, amount)
        cv.put(COLUMN_BUDGET_DATE, date)

        val result = db?.insert(BUDGET_TABLE, null, cv)

        return result
    }

    /** This method returns all the rows of a given table
     *  @param table - the table name
     *  @return returns a Cursor containing all the rows of the given table
     * */
    fun findAll(table: String): Cursor? {
        val query = "SELECT * FROM $table"
        val db = this.readableDatabase

        var cursor: Cursor? = null

        if (db!= null) {
            cursor = db.rawQuery(query, null)
        }
        
        return cursor
    }

    /** This method returns all the furniture in the furniture table
     *  @return returns an ArrayList<Furniture> of all the furniture inside the table
     * */
    fun findAllFurniture(): ArrayList<Furniture> {
        val query = "SELECT * FROM $FURNITURE_TABLE"
        val db = this.readableDatabase

        var cursor: Cursor? = null

        if (db!= null) {
            cursor = db.rawQuery(query, null)
        }

        val data = ArrayList<Furniture>()

        if (cursor != null && cursor.moveToFirst()) {
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

    /** This method returns all the furniture in the furniture table of a given room
     *  @param roomId - the row_id of the room
     *  @return returns an ArrayList<Furniture> of all the furniture of a given room
     * */
    fun findAllFurnitureOfRoom(roomId: String): ArrayList<Furniture> {
        val query = "SELECT * FROM $FURNITURE_TABLE WHERE $COLUMN_ROOM_ID = $roomId"
        val db = this.readableDatabase

        var cursor: Cursor? = null

        if (db!= null) {
            cursor = db.rawQuery(query, null)
        }

        val data = ArrayList<Furniture>()

        if (cursor != null && cursor.moveToFirst()) {
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

    /**
     * This method finds all the expenses between two dates (inclusive)
     * @param start the start date (yyyy-MM-dd)
     * @param end the end date (yyyy-MM-dd)
     * @return returns an arraylist of expenses
     * */
    fun findAllExpensesBetween(start: String, end: String): ArrayList<Expense> {
        val query = "SELECT * " +
                "FROM $EXPENSE_TABLE " +
                "WHERE $COLUMN_EXPENSE_DATE BETWEEN \'$start\' AND \'$end\' " +
                "ORDER BY $COLUMN_EXPENSE_DATE DESC"

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
    fun findAggExpensesBetween(
        start: String,
        end: String,
        columns: ArrayList<String>?,
        agg: ArrayList<HashMap<String, String>>,
        groups: ArrayList<String>,
        orderColumn: String?,
        orderType: String?,
        limit: Int?
    ): Cursor? {
        var query = StringBuilder()
        val db = readableDatabase
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

        query.append("FROM $EXPENSE_TABLE ")
        query.append("WHERE $COLUMN_EXPENSE_DATE BETWEEN \'$start\' AND \'$end\' ")

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
    fun findRecentExpenses(): ArrayList<Expense> {
        val query = "SELECT * " +
                "FROM $EXPENSE_TABLE " +
                "ORDER BY $COLUMN_EXPENSE_DATE DESC " +
                "LIMIT 3"

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

    /** This method updates the row of a room
     *  @param row_id - the row_id of the room to be updated
     *  @param month - the month (1-index based) of the room
     *  @param year - the year of the room
     *  @return returns true if the record has been updated. Otherwise, returns false
     * */
    fun updateRoom(row_id: String, month: Int, year: Int): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(COLUMN_ROOM_MONTH, month)
        cv.put(COLUMN_ROOM_YEAR, year)

        val result = db.update(ROOM_TABLE, cv, "$COLUMN_ROOM_ID=?", arrayOf(row_id))

        return result != -1
    }

    /** This method updates the row of a piece of furniture
     *  @param furniture - the furniture containing the updated data (also contains the row_id)
     *  @return returns true if the record has been updated. Otherwise, returns false
     * */
    fun updateFurniture(furniture: Furniture): Boolean {
        val db = this.writableDatabase
        val cv = ContentValues();

        cv.put(COLUMN_FURNITURE_TYPE, furniture.type)
        cv.put(COLUMN_FURNITURE_NAME, furniture.name)
        cv.put(COLUMN_FURNITURE_PRICE, furniture.price)
        cv.put(COLUMN_FURNITURE_EQUIPPED, if(furniture.equipped) 1 else 0)
        cv.put(COLUMN_FURNITURE_OWNED, if(furniture.owned) 1 else 0)

        val result = db.update(FURNITURE_TABLE, cv, "$COLUMN_FURNITURE_ID=?", arrayOf(furniture.furnitureId))

        return result != -1
    }

    /** This method updates the row of a budget
     *  @param rowId - the id of the budget
     *  @param amount - the updated amount of the budget
     *  @return returns true if the record has been updated. Otherwise, returns false
     * */
    fun updateBudget(rowId: String, amount: Float): Boolean{
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(COLUMN_BUDGET_AMOUNT, amount)

        val result = db.update(BUDGET_TABLE, cv, "$COLUMN_BUDGET_ID=?", arrayOf(rowId))

        return result != -1
    }

    /** This method updates the row of an expense
     *  @param expense - the expense containing the updated data (also contains the row_id)
     *  @return returns true if the record has been updated. Otherwise, returns false
     * */
    fun updateExpense(expense: Expense): Boolean{
        val db = this.writableDatabase
        val cv = ContentValues()

        cv.put(COLUMN_EXPENSE_TYPE, expense.type)
        cv.put(COLUMN_EXPENSE_AMOUNT, expense.amount)
        cv.put(COLUMN_EXPENSE_DESC, expense.desc)

        val result = db.update(EXPENSE_TABLE, cv, "$COLUMN_EXPENSE_ID=?", arrayOf(expense.expenseId))

        return result != -1
    }

    /** This method deletes an Expense
     *  @param rowId - the row_id of the expense to be deleted
     *  @return returns true if the record has been deleted. Otherwise, returns false
     * */
    fun deleteExpense(rowId: String):Boolean {
        val db = this.writableDatabase
        val result = db.delete(EXPENSE_TABLE, "$COLUMN_EXPENSE_ID=?", arrayOf(rowId))

        return result != -1
    }

    /** This method deletes a Budget
     *  @param rowId - the row_id of the budget to be deleted
     *  @return returns true if the record has been deleted. Otherwise, returns false
     * */
    fun deleteBudget(rowId: String): Boolean {
        val db = this.writableDatabase
        val result = db.delete(BUDGET_TABLE, "$COLUMN_BUDGET_ID=?", arrayOf(rowId))

        return result != -1
    }

    /** This method deletes a Room
     *  @param rowId - the row_id of the room to be deleted
     *  @return returns true if the record has been deleted. Otherwise, returns false
     * */
    fun deleteRoom (rowId: String): Boolean {
        val db = this.writableDatabase
        val result = db.delete(ROOM_TABLE, "$COLUMN_ROOM_ID=?", arrayOf(rowId))

        return result != -1
    }

    /** This method deletes a Furniture
     *  @param rowId - the row_id of the furniture to be deleted
     *  @return returns true if the record has been deleted. Otherwise, returns false
     * */
    fun deleteFurniture(rowId: String): Boolean {
        val db = this.writableDatabase
        val result = db.delete(FURNITURE_TABLE, "$COLUMN_FURNITURE_ID=?", arrayOf(rowId))

        return result != -1
    }

}