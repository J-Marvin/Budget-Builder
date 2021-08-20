package com.mobdeve.s13.group1.budgetbuilder

enum class ExpenseType(val iconColor: String, val backColor: String, val iconImg: Int) {
    SHOPPING("#FB7414", "#F6C376", R.drawable.shopping_basket),
    FOOD("#CF3B3B", "#EDC4C4", R.drawable.restaurant),
    TRANSPORTATION("#FF6200EE", "#FFD7B8FB", R.drawable.car)
}