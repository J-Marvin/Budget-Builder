package com.mobdeve.s13.group1.budgetbuilder

class Furniture (
    val imageId: Int,
    val price: Int,
    var owned: Boolean,
    var equipped: Boolean,
    var name: String,
    var type: String
) {

    lateinit var roomId: String
    lateinit var furnitureId: String

    fun equip() {
        this.equipped = true
    }

    fun purchase() {
        this.owned = true
    }
}