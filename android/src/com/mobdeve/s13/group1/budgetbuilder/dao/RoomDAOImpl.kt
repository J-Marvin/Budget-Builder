package com.mobdeve.s13.group1.budgetbuilder.dao

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.mobdeve.s13.group1.budgetbuilder.DataHelper

/** This is an implementation of the Room Data Access Object */
class RoomDAOImpl(context: Context): RoomDAO {

    companion object {
        /** This method inserts a room in the database
         *  @param db - the database
         *  @param month - the month for the room(1-based index)
         *  @param year - the year for the room
         *  @return returns the row_id of the room
         * */
        fun addRoom(db: SQLiteDatabase?, month: Int, year: Int): Long? {
            val cv = ContentValues()

            cv.put(DbReferences.COLUMN_ROOM_MONTH, month)
            cv.put(DbReferences.COLUMN_ROOM_YEAR, year)

            return db?.insert(DbReferences.ROOM_TABLE, null, cv)
        }
    }


    private val db: BudgetBuilderDbHelper

    init {
        db = BudgetBuilderDbHelper.getInstance(context)
    }

    override fun addRoom(month: Int, year: Int): Long {
        val db = db.writableDatabase
        val cv = ContentValues()

        cv.put(DbReferences.COLUMN_ROOM_MONTH, month)
        cv.put(DbReferences.COLUMN_ROOM_YEAR, year)

        return db.insert(DbReferences.ROOM_TABLE, null, cv)
    }

    override fun updateRoom(room: RoomModel): Boolean {
        val db = db.writableDatabase
        val cv = ContentValues()

        cv.put(DbReferences.COLUMN_ROOM_MONTH, room.month)
        cv.put(DbReferences.COLUMN_ROOM_YEAR, room.year)
        cv.put(DbReferences.COLUMN_ROOM_PATH, room.path)
        cv.put(DbReferences.COLUMN_ROOM_NAME, room.name)

        val result = db.update(DbReferences.ROOM_TABLE, cv, "${DbReferences.COLUMN_ROOM_ID}=?", arrayOf(room.id))

        return result != -1
    }

    override fun getRoom(id: String): RoomModel? {
        val db = db.readableDatabase
        val cursor = db.rawQuery(DbReferences.FIND_ROOM_BY_ID, arrayOf(id))
        var room: RoomModel? = null

        if (cursor != null && cursor.moveToFirst()) {
            val path = cursor.getString(cursor.getColumnIndex(DbReferences.COLUMN_ROOM_PATH))
            val name = cursor.getString(cursor.getColumnIndex(DbReferences.COLUMN_ROOM_NAME))
            room = RoomModel(
                cursor.getInt(cursor.getColumnIndex(DbReferences.COLUMN_ROOM_MONTH)),
                cursor.getInt(cursor.getColumnIndex(DbReferences.COLUMN_ROOM_YEAR)),
                cursor.getLong(cursor.getColumnIndex(DbReferences.COLUMN_ROOM_ID)).toString()
            )
            room.path = path
            room.name = name

        }

        return room
    }

    override fun getRoomByDate(month: Int, year: Int): RoomModel? {
        val db = db.readableDatabase
        val cursor = db.rawQuery(DbReferences.FIND_ROOM_BY_DATE, arrayOf(month.toString(), year.toString()))
        var room: RoomModel? = null

        if (cursor != null && cursor.moveToFirst()) {
            room = RoomModel(
                cursor.getInt(cursor.getColumnIndex(DbReferences.COLUMN_ROOM_MONTH)),
                cursor.getInt(cursor.getColumnIndex(DbReferences.COLUMN_ROOM_YEAR)),
                cursor.getLong(cursor.getColumnIndex(DbReferences.COLUMN_ROOM_ID)).toString()
            )

            cursor.close()
        }

        return room
    }

    override fun getAllRooms(): ArrayList<RoomModel> {
        val db = db.readableDatabase
        val cursor = db.rawQuery(DbReferences.FIND_ALL_ROOMS, null)
        val rooms = ArrayList<RoomModel>()

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val room = RoomModel(
                    cursor.getInt(cursor.getColumnIndex(DbReferences.COLUMN_ROOM_MONTH)),
                    cursor.getInt(cursor.getColumnIndex(DbReferences.COLUMN_ROOM_YEAR)),
                    cursor.getLong(cursor.getColumnIndex(DbReferences.COLUMN_ROOM_ID)).toString()
                    )
                val path = cursor.getString(cursor.getColumnIndex(DbReferences.COLUMN_ROOM_PATH))
                val name = cursor.getString(cursor.getColumnIndex(DbReferences.COLUMN_ROOM_NAME))
                room.path = path
                room.name = name
                rooms.add(room)
            } while (cursor.moveToNext())

            cursor.close()
        }

        return rooms
    }

    fun getPreviousRooms(): ArrayList<RoomModel> {

        val db = db.readableDatabase
        val cursor = db.rawQuery(DbReferences.FIND_PREVIOUS_ROOMS, null)
        val rooms = ArrayList<RoomModel>()

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val room = RoomModel(
                    cursor.getInt(cursor.getColumnIndex(DbReferences.COLUMN_ROOM_MONTH)),
                    cursor.getInt(cursor.getColumnIndex(DbReferences.COLUMN_ROOM_YEAR)),
                    cursor.getLong(cursor.getColumnIndex(DbReferences.COLUMN_ROOM_ID)).toString()
                )
                val path = cursor.getString(cursor.getColumnIndex(DbReferences.COLUMN_ROOM_PATH))
                val name = cursor.getString(cursor.getColumnIndex(DbReferences.COLUMN_ROOM_NAME))
                room.path = path
                room.name = name
                rooms.add(room)
            } while (cursor.moveToNext())

            cursor.close()
        }

        return rooms
    }

    override fun deleteRoom (rowId: String): Boolean {
        val db = db.writableDatabase
        val result = db.delete(DbReferences.ROOM_TABLE, "${DbReferences.COLUMN_ROOM_ID}=?", arrayOf(rowId))

        return result != -1
    }

    override fun initRoomFurniture(month: Int, year: Int): Long {
        val db = db.writableDatabase
        val roomId = this.addRoom(month, year)
        val furniture = DataHelper.getFurniture()

        if (roomId != -1L) {
            for(item in furniture){
                item.roomId = roomId.toString()
                FurnitureDAOImpl.addFurniture(db, item)
            }
        }

        return roomId
    }
}