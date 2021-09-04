package com.mobdeve.s13.group1.budgetbuilder.dao

class FurnitureModel (
    val imageId: Int,
    val price: Int,
    var owned: Boolean,
    var equipped: Boolean,
    var name: String,
    var type: String
) {

    lateinit var roomId: String
    lateinit var furnitureId: String

    constructor(furnitureId: String): this(0, 0, false, false, "", "") {
        this.furnitureId = furnitureId
    }
    fun equip() {
        this.equipped = true
    }

    fun purchase() {
        this.owned = true
    }

    override fun equals(other: Any?): Boolean {
        return if (other is FurnitureModel)
            this.furnitureId == other.furnitureId
        else false
    }
}