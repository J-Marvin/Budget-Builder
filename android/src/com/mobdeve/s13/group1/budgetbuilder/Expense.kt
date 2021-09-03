package com.mobdeve.s13.group1.budgetbuilder

import java.util.*

class Expense(
    var date: Date,
    var type: String,
    var amount: Float,
    var desc: String
    ) {

    var expenseId: String? = null
    var budgetId: String? = null
}