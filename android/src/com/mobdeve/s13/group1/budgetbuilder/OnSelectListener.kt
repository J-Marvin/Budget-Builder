package com.mobdeve.s13.group1.budgetbuilder

/**
 * This interface implements the listener when a room is selected in the gallery
 */
interface OnSelectListener {
    /**
     * This function implements the action when a room is selected in the gallery
     * @param name the name of the room
     * @param path the path to where the screenshot is stored
     */
    fun onSelect(name: String, path: String)
}