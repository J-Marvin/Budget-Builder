package com.mobdeve.s13.group1.budgetbuilder.dao

import java.util.ArrayList

interface FurnitureDAO {
    fun addFurniture(furniture: FurnitureModel): Long
    fun updateFurniture(furniture: FurnitureModel): Boolean
    fun findAllFurniture(): ArrayList<FurnitureModel>
    fun findAllFurnitureByRoom(id: String): ArrayList<FurnitureModel>
    fun findEquippedFurnitureByRoom(id: String): ArrayList<FurnitureModel>
    fun findOwnedFurnitureByRoom(id: String): ArrayList<FurnitureModel>
    fun deleteFurniture(id: String): Boolean
}