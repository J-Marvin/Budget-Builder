package com.mobdeve.s13.group1.budgetbuilder.dao

import java.util.*

class ExpenseModel {

    var expenseId: String? = null
    var budgetId: String? = null
    var date: Date? = null
    var type: String? = null
    var amount: Float? = null
    var desc: String? = null

    constructor(date: Date, type: String, amount: Float, desc: String) {
        this.date = date
        this.type = type
        this.amount = amount
        this.desc = desc
    }

    constructor(date: Date, type: String, amount: Float, desc: String, expenseId: String) {
        this.date = date
        this.type = type
        this.amount = amount
        this.desc = desc
        this.expenseId = expenseId
    }

    constructor(date: Date, type: String, amount: Float, desc: String, expenseId: String, budgetId: String) {
        this.date = date
        this.type = type
        this.amount = amount
        this.desc = desc
        this.expenseId = expenseId
        this.budgetId = budgetId
    }
}