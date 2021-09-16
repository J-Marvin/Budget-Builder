package com.mobdeve.s13.group1.budgetbuilder.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import java.util.ArrayList

/** This is an implementation of the Furniture Data Access Object */
class FurnitureDAOImpl(context: Context): FurnitureDAO {

    companion object{
        /** This method adds one furniture to a given database
         *  @param db - the database
         *  @param furniture - the furniture to be inserted
         *  @return returns the row_id of the furniture
         * */
        fun addFurniture(db: SQLiteDatabase?, furniture: FurnitureModel): Long? {
            val cv = ContentValues()

            cv.put(DbReferences.COLUMN_FURNITURE_TYPE, furniture.type)
            cv.put(DbReferences.COLUMN_ROOM_ID, furniture.roomId)
            cv.put(DbReferences.COLUMN_FURNITURE_NAME, furniture.name)
            cv.put(DbReferences.COLUMN_FURNITURE_PRICE, furniture.price)
            cv.put(DbReferences.COLUMN_FURNITURE_EQUIPPED, if (furniture.equipped) 1 else 0)
            cv.put(DbReferences.COLUMN_FURNITURE_OWNED, if (furniture.owned) 1 else 0)
            cv.put(DbReferences.COLUMN_FURNITURE_IMG, furniture.imageId)
            cv.put(DbReferences.COLUMN_FURNITURE_ASSET, furniture.path)
            cv.put(DbReferences.COLUMN_FURNITURE_ROOMASSET, furniture.roompath)

            return db?.insert(DbReferences.FURNITURE_TABLE, null, cv)
        }
    }
    private val db: BudgetBuilderDbHelper

    init {
        db = BudgetBuilderDbHelper.getInstance(context)
    }

    override fun addFurniture(furniture: FurnitureModel): Long {
        val db = db.writableDatabase
        val cv = ContentValues()

        cv.put(DbReferences.COLUMN_FURNITURE_TYPE, furniture.type)
        cv.put(DbReferences.COLUMN_FURNITURE_NAME, furniture.name)
        cv.put(DbReferences.COLUMN_FURNITURE_PRICE, furniture.price)
        cv.put(DbReferences.COLUMN_FURNITURE_EQUIPPED, if (furniture.equipped) 1 else 0)
        cv.put(DbReferences.COLUMN_FURNITURE_OWNED, if (furniture.owned) 1 else 0)
        cv.put(DbReferences.COLUMN_FURNITURE_IMG, furniture.imageId)
        cv.put(DbReferences.COLUMN_FURNITURE_ASSET, furniture.path)
        cv.put(DbReferences.COLUMN_FURNITURE_ROOMASSET, furniture.roompath)

        return db.insert(DbReferences.FURNITURE_TABLE, null, cv)
    }

    override fun updateFurniture(furniture: FurnitureModel): Boolean {
        val db = db.writableDatabase
        val cv = ContentValues()

        cv.put(DbReferences.COLUMN_FURNITURE_TYPE, furniture.type)
        cv.put(DbReferences.COLUMN_FURNITURE_NAME, furniture.name)
        cv.put(DbReferences.COLUMN_FURNITURE_PRICE, furniture.price)
        cv.put(DbReferences.COLUMN_FURNITURE_EQUIPPED, if(furniture.equipped) 1 else 0)
        cv.put(DbReferences.COLUMN_FURNITURE_OWNED, if(furniture.owned) 1 else 0)

        val result = db.update(DbReferences.FURNITURE_TABLE, cv, "${DbReferences.COLUMN_FURNITURE_ID}=?", arrayOf(furniture.furnitureId))

        return result != -1
    }

    override fun findAllFurniture(): ArrayList<FurnitureModel> {
        val db = db.readableDatabase

        var cursor: Cursor? = null

        if (db!= null) {
            cursor = db.rawQuery(DbReferences.FIND_ALL_FURNITURE, null)
        }

        val data = ArrayList<FurnitureModel>()

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val furniture = FurnitureModel(
                    cursor.getInt(cursor.getColumnIndex(DbReferences.COLUMN_FURNITURE_IMG)),
                    cursor.getInt(cursor.getColumnIndex(DbReferences.COLUMN_FURNITURE_PRICE)),
                    owned = cursor.getInt(cursor.getColumnIndex(DbReferences.COLUMN_FURNITURE_OWNED)) == 1,
                    equipped = cursor.getInt(cursor.getColumnIndex(DbReferences.COLUMN_FURNITURE_EQUIPPED)) == 1,
                    cursor.getString(cursor.getColumnIndex(DbReferences.COLUMN_FURNITURE_NAME)),
                    cursor.getString(cursor.getColumnIndex(DbReferences.COLUMN_FURNITURE_TYPE)),
                    cursor.getString(cursor.getColumnIndex(DbReferences.COLUMN_FURNITURE_ASSET)),
                    cursor.getString(cursor.getColumnIndex(DbReferences.COLUMN_FURNITURE_ROOMASSET))
                )

                furniture.roomId = cursor.getInt(cursor.getColumnIndex(DbReferences.COLUMN_ROOM_ID)).toString()
                furniture.furnitureId = cursor.getInt(cursor.getColumnIndex(DbReferences.COLUMN_FURNITURE_ID)).toString()

                data.add(furniture)
            } while (cursor.moveToNext())

            cursor.close()
        }

        return data
    }

    override fun findAllFurnitureByRoom(roomId: String): ArrayList<FurnitureModel> {
        val db = db.readableDatabase

        var cursor: Cursor? = null

        if (db!= null) {
            cursor = db.rawQuery(DbReferences.FIND_FURNITURE_BY_ROOM, arrayOf(roomId))
        }

        val data = ArrayList<FurnitureModel>()

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val furniture = FurnitureModel(
                    cursor.getInt(cursor.getColumnIndex(DbReferences.COLUMN_FURNITURE_IMG)),
                    cursor.getInt(cursor.getColumnIndex(DbReferences.COLUMN_FURNITURE_PRICE)),
                    owned = cursor.getInt(cursor.getColumnIndex(DbReferences.COLUMN_FURNITURE_OWNED)) == 1,
                    equipped = cursor.getInt(cursor.getColumnIndex(DbReferences.COLUMN_FURNITURE_EQUIPPED)) == 1,
                    cursor.getString(cursor.getColumnIndex(DbReferences.COLUMN_FURNITURE_NAME)),
                    cursor.getString(cursor.getColumnIndex(DbReferences.COLUMN_FURNITURE_TYPE)),
                    cursor.getString(cursor.getColumnIndex(DbReferences.COLUMN_FURNITURE_ASSET)),
                    cursor.getString(cursor.getColumnIndex(DbReferences.COLUMN_FURNITURE_ROOMASSET))
                )

                furniture.roomId = cursor.getInt(cursor.getColumnIndex(DbReferences.COLUMN_ROOM_ID)).toString()
                furniture.furnitureId = cursor.getInt(cursor.getColumnIndex(DbReferences.COLUMN_FURNITURE_ID)).toString()

                data.add(furniture)
            } while (cursor.moveToNext())

            cursor.close()
        }

        return data
    }

    override fun findEquippedFurnitureByRoom(roomId: String): ArrayList<FurnitureModel> {
        val db = db.readableDatabase

        var cursor: Cursor? = null

        if (db!= null) {
            cursor = db.rawQuery(DbReferences.FIND_EQUIPPED_FURNITURE_BY_ROOM, arrayOf(roomId))
        }

        val data = ArrayList<FurnitureModel>()

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val furniture = FurnitureModel(
                    cursor.getInt(cursor.getColumnIndex(DbReferences.COLUMN_FURNITURE_IMG)),
                    cursor.getInt(cursor.getColumnIndex(DbReferences.COLUMN_FURNITURE_PRICE)),
                    owned = cursor.getInt(cursor.getColumnIndex(DbReferences.COLUMN_FURNITURE_OWNED)) == 1,
                    equipped = cursor.getInt(cursor.getColumnIndex(DbReferences.COLUMN_FURNITURE_EQUIPPED))  == 1,
                    cursor.getString(cursor.getColumnIndex(DbReferences.COLUMN_FURNITURE_NAME)),
                    cursor.getString(cursor.getColumnIndex(DbReferences.COLUMN_FURNITURE_TYPE)),
                    cursor.getString(cursor.getColumnIndex(DbReferences.COLUMN_FURNITURE_ASSET)),
                    cursor.getString(cursor.getColumnIndex(DbReferences.COLUMN_FURNITURE_ROOMASSET))
                )
                furniture.roomId = cursor.getInt(cursor.getColumnIndex(DbReferences.COLUMN_ROOM_ID)).toString()
                furniture.furnitureId = cursor.getInt(cursor.getColumnIndex(DbReferences.COLUMN_FURNITURE_ID)).toString()
                data.add(furniture)
            } while (cursor.moveToNext())

            cursor.close()
        }

        return data
    }

    override fun findOwnedFurnitureByRoom(roomId: String): ArrayList<FurnitureModel> {
        val db = db.readableDatabase

        var cursor: Cursor? = null

        if (db!= null) {
            cursor = db.rawQuery(DbReferences.FIND_OWNED_FURNITURE_BY_ROOM, arrayOf(roomId))
        }

        val data = ArrayList<FurnitureModel>()

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val furniture = FurnitureModel(
                    cursor.getInt(cursor.getColumnIndex(DbReferences.COLUMN_FURNITURE_IMG)),
                    cursor.getInt(cursor.getColumnIndex(DbReferences.COLUMN_FURNITURE_PRICE)),
                    owned = cursor.getInt(cursor.getColumnIndex(DbReferences.COLUMN_FURNITURE_OWNED)) == 1,
                    equipped = cursor.getInt(cursor.getColumnIndex(DbReferences.COLUMN_FURNITURE_EQUIPPED))  == 1,
                    cursor.getString(cursor.getColumnIndex(DbReferences.COLUMN_FURNITURE_NAME)),
                    cursor.getString(cursor.getColumnIndex(DbReferences.COLUMN_FURNITURE_TYPE)),
                    cursor.getString(cursor.getColumnIndex(DbReferences.COLUMN_FURNITURE_ASSET)),
                    cursor.getString(cursor.getColumnIndex(DbReferences.COLUMN_FURNITURE_ROOMASSET))
                )
                furniture.roomId = cursor.getInt(cursor.getColumnIndex(DbReferences.COLUMN_ROOM_ID)).toString()
                furniture.furnitureId = cursor.getInt(cursor.getColumnIndex(DbReferences.COLUMN_FURNITURE_ID)).toString()
                data.add(furniture)
            } while (cursor.moveToNext())

            cursor.close()
        }

        return data
    }


    override fun deleteFurniture(rowId: String): Boolean {
        val db = db.writableDatabase
        val result = db.delete(DbReferences.FURNITURE_TABLE, "${DbReferences.COLUMN_FURNITURE_ID}=?", arrayOf(rowId))

        return result != -1
    }
}