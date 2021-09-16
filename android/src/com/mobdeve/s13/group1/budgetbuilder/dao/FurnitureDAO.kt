package com.mobdeve.s13.group1.budgetbuilder.dao

import java.util.ArrayList

/** This interface represents a DataAccessObject for the Furniture Table */
interface FurnitureDAO {
    /** This method inserts one furniture inside the database
     *  @param furniture - the furniture to be inserted
     *  @return returns the row_id of the furniture
     * */
    fun addFurniture(furniture: FurnitureModel): Long

    /** This method updates the row of a piece of furniture
     *  @param furniture - the furniture containing the updated data (also contains the row_id)
     *  @return returns true if the record has been updated. Otherwise, returns false
     * */
    fun updateFurniture(furniture: FurnitureModel): Boolean

    /** This method returns all the furniture in the furniture table
     *  @return returns an ArrayList<Furniture> of all the furniture inside the table
     * */
    fun findAllFurniture(): ArrayList<FurnitureModel>

    /** This method returns all the furniture in the furniture table of a given room
     *  @param roomId - the row_id of the room
     *  @return returns an ArrayList<Furniture> of all the furniture of a given room
     * */
    fun findAllFurnitureByRoom(roomId: String): ArrayList<FurnitureModel>

    /** This method returns all the equipped furniture of a given room
     *  @param roomId - the row_id of the room
     *  @return returns an ArrayList<Furniture> of all the equipped furniture of a given room
     * */
    fun findEquippedFurnitureByRoom(roomId: String): ArrayList<FurnitureModel>

    /** This method returns all the equipped furniture of a given room
     *  @param roomId - the row_id of the room
     *  @return returns an ArrayList<Furniture> of all the equipped furniture of a given room
     * */
    fun findOwnedFurnitureByRoom(roomId: String): ArrayList<FurnitureModel>

    /** This method deletes a Furniture
     *  @param rowId - the row_id of the furniture to be deleted
     *  @return returns true if the record has been deleted. Otherwise, returns false
     * */
    fun deleteFurniture(rowId: String): Boolean
}