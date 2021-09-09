package com.mobdeve.s13.group1.budgetbuilder.dao

import android.graphics.drawable.Drawable

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
    lateinit var path: String
    lateinit var roompath: String

    constructor(furnitureId: String): this(0, 0, false, false, "", "") {
        this.furnitureId = furnitureId
    }

    constructor(imageId: Int,
                price: Int,
                owned: Boolean,
                equipped: Boolean,
                name: String,
                type: String,
                path: String,
                roompath: String): this(imageId, price, owned, equipped, name, type) {
        this.path = path
        this.roompath = roompath
    }

    fun equip() {
        this.equipped = true
    }

    fun unequip(){
        this.equipped = false
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