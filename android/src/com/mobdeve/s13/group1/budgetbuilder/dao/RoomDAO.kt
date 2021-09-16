package com.mobdeve.s13.group1.budgetbuilder.dao

/** This interface represents a DataAccessObject for the Room Table */
interface RoomDAO {

    /** This method inserts a room in the database
     *  @param month - the month for the room(1-based index)
     *  @param year - the year for the room
     *  @return returns the row_id of the room
     * */
    fun addRoom(month: Int, year: Int): Long

    /** This method updates the row of a room
     *  @param room - the RoomModel object containing the information of the room
     *  @return returns true if the record has been updated. Otherwise, returns false
     * */
    fun updateRoom(room: RoomModel): Boolean

    /** This method gets a room from the database
     *  @param id - the row_id of the room
     *  @return returns the RoomModel object containing the Room information if it exists
     * */
    fun getRoom(id: String): RoomModel?

    /** This method deletes a Room
     *  @param rowId - the row_id of the room to be deleted
     *  @return returns true if the record has been deleted. Otherwise, returns false
     * */
    fun deleteRoom(rowId: String): Boolean

    /** This method inserts a room in the database and initializes all the furniture
     *  @param month - the month for the room(1-based index)
     *  @param year - the year for the room
     *  @return returns the row_id of the room
     * */
    fun initRoomFurniture(month: Int, year: Int): Long

    /** This method gets a room based on its date
     *  @param month - the month
     *  @param year - the year
     *  @return returns the room model if it exists else, returns null
     * */
    fun getRoomByDate(month: Int, year: Int): RoomModel?

    /** This method gets all the rooms in the database
     *   @return returns an arraylist of all the rooms
     * */
    fun getAllRooms(): ArrayList<RoomModel>
}