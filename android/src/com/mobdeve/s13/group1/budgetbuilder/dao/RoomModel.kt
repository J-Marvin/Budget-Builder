package com.mobdeve.s13.group1.budgetbuilder.dao

class RoomModel {
    var img: Int? = null
    var name: String? = null
    var month: Int? = null
    var year: Int? = null
    var id: String? = null

    constructor(img: Int, name: String) {
        this.img = img

        this.name = name
    }

    constructor()

    constructor(img: Int, name: String, month: Int, year: Int) {
        this.img = img
        this.name = name
        this.month = month
        this.year = year
    }

    constructor(month: Int, year: Int, id: String) {
        this.month = month
        this.year = year
        this.id = id
    }
}