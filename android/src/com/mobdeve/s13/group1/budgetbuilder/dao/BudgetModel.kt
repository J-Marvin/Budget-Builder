package com.mobdeve.s13.group1.budgetbuilder.dao

/** This class represents a Budget with its amount, date and id
 * */
class BudgetModel {
    var budget: Float? = null
    var date: String? = null
    var id: String? = null

    constructor()
    constructor(budget: Float, date: String) {
        this.budget = budget
        this.date = date
    }

    constructor(budget: Float, date: String, id: String) {
        this.budget = budget
        this.date = date
        this.id = id
    }
}