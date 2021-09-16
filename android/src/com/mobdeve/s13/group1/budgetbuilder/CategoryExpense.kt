package com.mobdeve.s13.group1.budgetbuilder

/** This class represents a category expense or an expense with only its type, color, total and percent of the budget*/
class CategoryExpense(
    var type: String,
    var color: Int
) {
    var total: Float = 0F
    var percent: Int = 0
}