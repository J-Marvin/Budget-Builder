package com.mobdeve.s13.group1.budgetbuilder.dao

import java.text.SimpleDateFormat

class DbReferences {
    companion object{
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
        const val COLUMN_EXPENSE_DESC = "expense_desc"
        const val COLUMN_EXPENSE_DATE = "date"

        const val AGG_AVG = "avg"
        const val AGG_COUNT = "count"
        const val AGG_GROUP_CONCAT = "group_concat"
        const val AGG_MAX = "max"
        const val AGG_MIN = "min"
        const val AGG_SUM = "sum"
        const val AGG_TOTAL = "total"

        const val COLUMN_AGG_SUM = "sum"

        val aggList = arrayOf("avg", "count", "max", "min", "sum", "total")

        const val CREATE_ROOM_TABLE = "CREATE TABLE $ROOM_TABLE (" +
                "$COLUMN_ROOM_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_ROOM_MONTH INTEGER NOT NULL," +
                "$COLUMN_ROOM_YEAR INTEGER NOT NULL)"

        const val CREATE_FURNITURE_TABLE = "CREATE TABLE $FURNITURE_TABLE (" +
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

        const val CREATE_BUDGET_TABLE = "CREATE TABLE $BUDGET_TABLE (" +
                "$COLUMN_BUDGET_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_BUDGET_AMOUNT REAL NOT NULL," +
                "$COLUMN_BUDGET_DATE TEXT NOT NULL" +
                ")"

        const val CREATE_EXPENSE_TABLE = "CREATE TABLE $EXPENSE_TABLE (" +
                "$COLUMN_EXPENSE_ID INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                "$COLUMN_EXPENSE_TYPE TEXT NOT NULL," +
                "$COLUMN_EXPENSE_AMOUNT REAL NOT NULL," +
                "$COLUMN_EXPENSE_DESC TEXT NOT NULL," +
                "$COLUMN_EXPENSE_DATE TEXT NOT NULL," +
                "$COLUMN_BUDGET_ID INTEGER NOT NULL," +
                "FOREIGN KEY($COLUMN_BUDGET_ID) REFERENCES $BUDGET_TABLE($COLUMN_BUDGET_ID)" +
                ")"

        const val DROP_ROOM_TABLE = "DROP IF EXISTS $ROOM_TABLE"
        const val DROP_FURNITURE_TABLE = "DROP IF EXISTS $FURNITURE_TABLE"
        const val DROP_BUDGET_TABLE = "DROP IF EXISTS $BUDGET_TABLE"
        const val DROP_EXPENSE_TABLE = "DROP IF EXISTS $EXPENSE_TABLE"

        const val FIND_BUDGET_BY_DATE = "SELECT * FROM $BUDGET_TABLE WHERE $COLUMN_BUDGET_DATE = ?"
        const val FIND_BUDGET_BY_ID = "SELECT * FROM $BUDGET_TABLE WHERE $COLUMN_BUDGET_ID = ?"
        const val FIND_ALL_BUDGET = "SELECT * FROM $BUDGET_TABLE"
        const val FIND_EXPENSES_BY_DATE_DESC = "SELECT * " +
                "FROM $EXPENSE_TABLE " +
                "WHERE $COLUMN_EXPENSE_DATE BETWEEN ? AND ? " +
                "ORDER BY $COLUMN_EXPENSE_DATE DESC"

        const val FIND_EXPENSES_BY_DATE_ASC = "SELECT * " +
                "FROM $EXPENSE_TABLE " +
                "WHERE $COLUMN_EXPENSE_DATE BETWEEN ? AND ? " +
                "ORDER BY $COLUMN_EXPENSE_DATE"

        const val FIND_SUM_OF_EXPENSES_BY_DATE = "SELECT SUM($COLUMN_EXPENSE_AMOUNT) AS $COLUMN_AGG_SUM " +
                "FROM $EXPENSE_TABLE " +
                "WHERE $COLUMN_EXPENSE_DATE BETWEEN ? AND ? "

        const val FIND_DAILY_SUM_BY_MONTH = "SELECT strftime('%Y-%m-%d', $COLUMN_EXPENSE_DATE) AS $COLUMN_EXPENSE_DATE, " +
                "SUM($COLUMN_EXPENSE_AMOUNT) AS $COLUMN_AGG_SUM " +
                "FROM $EXPENSE_TABLE " +
                "WHERE $COLUMN_EXPENSE_DATE BETWEEN ? AND ?" +
                "GROUP BY strftime('%Y-%m-%d', $COLUMN_EXPENSE_DATE) "

        const val FIND_EXPENSES_BY_CATEGORY_BETWEEN_DATE = "SELECT ${COLUMN_EXPENSE_TYPE}, SUM($COLUMN_EXPENSE_AMOUNT) AS $COLUMN_AGG_SUM " +
                "FROM $EXPENSE_TABLE " +
                "WHERE $COLUMN_EXPENSE_DATE BETWEEN ? AND ? " +
                "GROUP BY $COLUMN_EXPENSE_TYPE"

        const val GET_SUM_BY_CATEGORY = "SELECT SUM($COLUMN_EXPENSE_AMOUNT) AS $COLUMN_AGG_SUM " +
                "FROM $EXPENSE_TABLE " +
                "WHERE $COLUMN_EXPENSE_DATE BETWEEN ? AND ? "

        const val FIND_ALL_FURNITURE = "SELECT * FROM $FURNITURE_TABLE"
        const val FIND_FURNITURE_BY_ROOM = "SELECT * FROM $FURNITURE_TABLE WHERE $COLUMN_ROOM_ID = ?"

        const val FIND_ALL_ROOMS = "SELECT * FROM $ROOM_TABLE"
        const val FIND_ROOM_BY_DATE = "SELECT * FROM $ROOM_TABLE WHERE $COLUMN_ROOM_MONTH = ? AND $COLUMN_ROOM_YEAR = ?"
        const val FIND_ROOM_BY_ID = "SELECT * FROM $ROOM_TABLE WHERE $COLUMN_ROOM_ID = ?"
    }
}