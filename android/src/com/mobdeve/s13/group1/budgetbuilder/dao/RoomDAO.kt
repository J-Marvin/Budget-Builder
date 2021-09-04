package com.mobdeve.s13.group1.budgetbuilder.dao

import android.content.Context

interface RoomDAO {
    fun addRoom(month: Int, year: Int): Long
    fun updateRoom(room: RoomModel): Boolean
    fun getRoom(id: String): RoomModel?
    fun deleteRoom(id: String): Boolean
}